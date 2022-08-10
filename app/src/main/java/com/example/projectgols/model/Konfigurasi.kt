package com.example.projectgols.model

class Konfigurasi {
    var ongkir: Int = 0
    var ppn: Int = 0
    var biaya_admin: Int = 0

    constructor() {}
    constructor(
        ongkir: Int, ppn: Int, biaya_admin: Int
    ) {
        this.ongkir = ongkir
        this.ppn = ppn
        this.biaya_admin = biaya_admin
    }
}