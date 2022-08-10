package com.example.projectgols.model

class KeranjangBarang {
    var keranjang: Keranjang = Keranjang()
    var barang: Barang = Barang()

    constructor() {}
    constructor(
        keranjang: Keranjang, barang: Barang
    ) {
        this.keranjang = keranjang
        this.barang = barang
    }
}