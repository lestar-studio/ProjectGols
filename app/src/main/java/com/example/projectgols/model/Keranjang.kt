package com.example.projectgols.model

class Keranjang {
    var id_keranjang: Int = 0
    var id_user: Int = 0
    var tgl_get: String = ""
    var qty: Int = 0
    var id_brg: Int = 0

    constructor() {}
    constructor(
        id_keranjang:Int, id_user:Int, tgl_get: String, qty: Int, id_brg: Int
    ) {
        this.id_keranjang = id_keranjang
        this.id_user = id_user
        this.tgl_get = tgl_get
        this.qty = qty
        this.id_brg = id_brg
    }
}