package com.example.projectgols.model

class DetailPesanan {
    var id_pesanan: String = ""
    var id_detail_pesanan: Int = 0
    var id_brg: Int = 0
    var qty: Int = 0

    constructor() {}
    constructor(
        id_pesanan:String, id_detail_pesanan: Int, id_brg: Int, qty: Int
    ) {
        this.id_pesanan = id_pesanan
        this.id_detail_pesanan = id_detail_pesanan
        this.id_brg = id_brg
        this.qty = qty
    }
}