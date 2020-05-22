package com.cepgamer.strattester.generator

interface Generator<T> {
    fun generate(): List<() -> T>
}