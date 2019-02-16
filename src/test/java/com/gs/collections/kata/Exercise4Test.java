/*
 * Copyright 2015 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.kata;

import java.util.List;
import java.util.ListIterator;

import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.list.primitive.DoubleList;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.primitive.DoublePredicates;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.primitive.DoubleLists;
import com.gs.collections.impl.list.fixed.ArrayAdapter;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.ListIterate;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import org.junit.Assert;
import org.junit.Test;

public class Exercise4Test extends CompanyDomainForKata
{
    /**
     * Solve this without changing the return type of {@link Company#getSuppliers()}. Find the appropriate method on
     * {@link ArrayIterate}.
     */
    @Test
    public void findSupplierNames() {
//        MutableList<String> supplierNames = ArrayIterate.collect(this.company.getSuppliers(), Supplier::getName);
        MutableList<String> supplierNames = ArrayAdapter.adapt(this.company.getSuppliers()).collect(Supplier::getName);

        MutableList<String> expectedSupplierNames = FastList.newListWith(
                "Shedtastic",
                "Splendid Crocks",
                "Annoying Pets",
                "Gnomes 'R' Us",
                "Furniture Hamlet",
                "SFD",
                "Doxins");
        Assert.assertEquals(expectedSupplierNames, supplierNames);
    }

    /**
     * Create a {@link Predicate} for Suppliers that supply more than 2 items. Find the number of suppliers that
     * satisfy that Predicate.
     */
    @Test
    public void countSuppliersWithMoreThanTwoItems()
    {
        Predicate<Supplier> moreThanTwoItems = supplier -> supplier.getItemNames().length > 2;
        int suppliersWithMoreThanTwoItems = ArrayAdapter.adapt(this.company.getSuppliers()).count(moreThanTwoItems);
        Assert.assertEquals("suppliers with more than 2 items", 5, suppliersWithMoreThanTwoItems);
    }

    /**
     * Try to solve this without changing the return type of {@link Supplier#getItemNames()}.
     */
    @Test
    public void whoSuppliesSandwichToaster() {
        // Create a Predicate that will check to see if a Supplier supplies a "sandwich toaster".
        Predicate<Supplier> suppliesToaster = supplier -> ArrayAdapter.adapt(supplier.getItemNames()).contains("sandwich toaster");

        // Find one supplier that supplies toasters.
        Supplier toasterSupplier = ArrayAdapter.adapt(this.company.getSuppliers()).detect(suppliesToaster);
        Assert.assertNotNull("toaster supplier", toasterSupplier);
        Assert.assertEquals("Doxins", toasterSupplier.getName());
    }

    @Test
    public void filterOrderValues()
    {
        List<Order> orders = this.company.getMostRecentCustomer().getOrders();
        /**
         * Get the order values that are greater than 1.5.
         */
//        MutableList<Double> orderValues = ListIterate.collect(orders, Order::getValue);
//        MutableList<Double> filtered = orderValues.select(Predicates.greaterThan(1.5));
//        Assert.assertEquals(FastList.newListWith(372.5, 1.75), filtered);

        DoubleList orderValuesPrimitive = ListIterate.collectDouble(orders, Order::getValue);
        DoubleList filteredPrimitive = orderValuesPrimitive.select(DoublePredicates.greaterThan(1.5));
        Assert.assertEquals(DoubleLists.mutable.of(372.5, 1.75), filteredPrimitive);
    }

    @Test
    public void filterOrders()
    {
        List<Order> orders = this.company.getMostRecentCustomer().getOrders();
        /**
         * Get the actual orders (not their double values) where those orders have a value greater than 2.0.
         */
        MutableList<Order> filtered = ListIterate.select(orders, Predicates.attributeGreaterThan(Order::getValue, 2.0));
        Assert.assertEquals(FastList.newListWith(Iterate.getFirst(this.company.getMostRecentCustomer().getOrders())), filtered);
    }
}
