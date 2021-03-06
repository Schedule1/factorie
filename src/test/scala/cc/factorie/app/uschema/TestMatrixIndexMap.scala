/* Copyright (C) 2008-2016 University of Massachusetts Amherst.
   This file is part of "FACTORIE" (Factor graphs, Imperative, Extensible)
   http://factorie.cs.umass.edu, http://github.com/factorie
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */
package cc.factorie.app.uschema

import org.scalatest.junit.JUnitSuite
import cc.factorie.util
import org.junit.Test
import org.junit.Assert._
import com.mongodb.DB
import com.github.fakemongo.Fongo
import scala.Some

/**
 * Created by beroth on 3/9/15.
 */
class TestMatrixIndexMap extends JUnitSuite  with util.FastLogging {
  @Test def readWriteMongoStringMapTest() {
    val smap = new StringMemoryIndexMap(collectionPrefix = MongoWritable.ENTITY_ROW_MAP_PREFIX)
    smap.add("b")
    smap.add("C")
    smap.add("d")
    smap.add("a")
    smap.add("b")
    smap.add("A")
    val fongo = new Fongo("myserver")
    val db : DB = fongo.getDB("mydb")

    smap.writeToMongo(db)

    val smap2 = new StringMemoryIndexMap(collectionPrefix = MongoWritable.ENTITY_ROW_MAP_PREFIX)
    smap2.populateFromMongo(db)

    assertEquals(smap.size, smap2.size)

    for(i <- 0 until smap.size) {
      assertEquals(smap.indexToKey(i), smap2.indexToKey(i))
    }
  }



  @Test def readWriteMongoEntityPairMapTest() {
    val emap = new EntityPairMemoryMap(collectionPrefix = MongoWritable.ENTITY_ROW_MAP_PREFIX)
    emap.add(new EntityPair("a","b"))
    emap.add(new EntityPair("A","B"))
    emap.add(new EntityPair("c","b"))
    emap.add(new EntityPair("C","D"))
    emap.add(new EntityPair("a","b"))

    val fongo = new Fongo("myserver")
    val db : DB = fongo.getDB("mydb2")

    emap.writeToMongo(db)
    val emap2 = new EntityPairMemoryMap(collectionPrefix = MongoWritable.ENTITY_ROW_MAP_PREFIX)
    emap2.populateFromMongo(db)

    assertEquals(emap.size, emap2.size)

    for(i <- 0 until emap.size) {
      assertEquals(emap.indexToKey(i), emap2.indexToKey(i))
    }
  }
}
