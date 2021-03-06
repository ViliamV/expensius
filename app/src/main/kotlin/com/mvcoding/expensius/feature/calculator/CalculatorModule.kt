/*
 * Copyright (C) 2015 Mantas Varnagiris.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.mvcoding.expensius.feature.calculator

import com.memoizrlabs.Shank.registerFactory
import com.memoizrlabs.ShankModule
import com.mvcoding.expensius.provideSettings
import java.math.BigDecimal

class CalculatorModule : ShankModule {
    override fun registerFactories() {
        calculatorPresenter()
    }

    private fun calculatorPresenter() = registerFactory(CalculatorPresenter::class.java, {
        initialNumber: BigDecimal?, resultDestination: CalculatorPresenter.ResultDestination ->
        CalculatorPresenter(Calculator(Interpreter()), resultDestination, provideSettings(), initialNumber)
    })
}