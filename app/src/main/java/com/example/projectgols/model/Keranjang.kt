package com.example.projectgols.model

class Keranjang {
    var id_keranjang: String = ""
    var id_user: String = ""
    var tgl_get: String = ""
    var qty: Int = 0
    var id_brg: Int = 0

    constructor() {}
    constructor(
        id_keranjang:String, id_user:String, tgl_get: String, qty: Int, id_brg: Int
    ) {
        this.id_keranjang = id_keranjang
        this.id_user = id_user
        this.tgl_get = tgl_get
        this.qty = qty
        this.id_brg = id_brg
    }
}