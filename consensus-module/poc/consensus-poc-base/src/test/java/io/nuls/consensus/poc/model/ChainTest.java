/*
 * *
 *  * MIT License
 *  *
 *  * Copyright (c) 2017-2019 nuls.io
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *
 */

package io.nuls.consensus.poc.model;

import io.nuls.kernel.model.Block;
import io.nuls.kernel.model.BlockHeader;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ln on 2018/5/7.
 */
public class ChainTest {

    @Test
    public void test() {
        Chain chain = new Chain();

        assertNotNull(chain.getId());

        assertNull(chain.getBestBlock());

        Block block = new Block();
        BlockHeader blockHeader = new BlockHeader();
        blockHeader.setHeight(100l);
        block.setHeader(blockHeader);
        chain.getAllBlockList().add(block);

        Block bestBlock = chain.getBestBlock();
        assertNotNull(bestBlock);
        assertEquals(bestBlock.getHeader().getHeight(), 100l);
    }
}