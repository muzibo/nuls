package io.nuls.consensus.service.impl;

import io.nuls.consensus.service.cache.BlockCacheService;
import io.nuls.consensus.service.intf.BlockService;
import io.nuls.consensus.utils.ConsensusTool;
import io.nuls.core.chain.entity.Block;
import io.nuls.core.chain.entity.BlockHeader;
import io.nuls.core.chain.entity.Transaction;
import io.nuls.core.constant.TransactionConstant;
import io.nuls.core.context.NulsContext;
import io.nuls.core.exception.NulsException;
import io.nuls.core.exception.NulsRuntimeException;
import io.nuls.core.utils.log.Log;
import io.nuls.db.dao.BlockDao;
import io.nuls.db.dao.ConsensusDao;
import io.nuls.db.entity.BlockPo;
import io.nuls.db.entity.TransactionPo;
import io.nuls.db.util.TransactionPoTool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Niels
 * @date 2017/12/11
 */
public class BlockServiceImpl implements BlockService {
    private static final BlockServiceImpl INSTANCE = new BlockServiceImpl();

    private BlockDao blockDao = NulsContext.getInstance().getService(BlockDao.class);
    private ConsensusDao consensusDao = NulsContext.getInstance().getService(ConsensusDao.class);
    private BlockCacheService blockCacheService = BlockCacheService.getInstance();

    private BlockServiceImpl() {
    }

    public static BlockServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Block getGengsisBlock() {
        BlockPo po = this.blockDao.getBlock(0);
        try {
            return ConsensusTool.fromPojo(po);
        } catch (NulsException e) {
            Log.error(e);
            return null;
        }
    }

    @Override
    public long getLocalHeight() {
        long height = blockCacheService.getMaxHeight();
        if (height == 0) {
            height = blockDao.queryMaxHeight();
        }
        return height;
    }

    @Override
    public Block getLocalBestBlock() {
        Block block = blockCacheService.getBlock(blockCacheService.getMaxHeight());
        if (null == block) {
            BlockPo po = blockDao.getHighestBlock();
            try {
                block = ConsensusTool.fromPojo(po);
            } catch (NulsException e) {
                Log.error(e);
                return null;
            }
        }
        return block;
    }

    @Override
    public BlockHeader getBlockHeader() {
        // todo auto-generated method stub(niels)
        return null;
    }

    @Override
    public Block getBlock(String hash) {
        Block block = blockCacheService.getBlock(hash);
        if (null == block) {
            BlockPo po = blockDao.getBlockByHash(hash);
            try {
                block = ConsensusTool.fromPojo(po);
            } catch (NulsException e) {
                Log.error(e);
                return null;
            }
        }
        return block;
    }

    @Override
    public Block getBlock(long height) {
        Block block = blockCacheService.getBlock(height);
        if (null == block) {
            BlockPo po = blockDao.getBlock(height);
            try {
                block = ConsensusTool.fromPojo(po);
            } catch (NulsException e) {
                Log.error(e);
                return null;
            }
        }
        return block;
    }


    @Override
    public void saveBlock(Block block) {
        BlockPo blockPo = ConsensusTool.toPojo(block);
        List<TransactionPo> txPoList = new ArrayList<>();
        for (int x = 0; x < block.getHeader().getTxCount(); x++) {
            Transaction tx = block.getTxs().get(x);
            try {
                tx.onCommit();
                txPoList.add(TransactionPoTool.toPojo(tx));
            } catch (Exception e) {
                Log.error(e);
                rollback(block.getTxs(), x);
                throw new NulsRuntimeException(e);
            }
        }
        consensusDao.blockPersistence(blockPo, txPoList);
    }

    @Override
    public void rollbackBlock(long height) {
        Block block = this.getBlock(height);
        if (null == block) {
            return;
        }
        this.rollback(block.getTxs(), block.getTxs().size() - 1);
        blockDao.deleteByKey(block.getHeader().getHash().getDigestHex());
    }

    @Override
    public int getBlockCount(String address, long roundStart, long roundEnd) {
        return this.blockDao.count(address, roundStart, roundEnd);
    }

    private void rollback(List<Transaction> txs, int max) {
        for (int x = 0; x < max; x++) {
            Transaction tx = txs.get(x);
            try {
                tx.onRollback();
            } catch (NulsException e) {
                Log.error(e);
            }
        }

    }
}
