package com.example.projectgols.model

class Barang {
    var id_brg: Int = 0
    lateinit var nama_brg: String
    lateinit var deskripsi: String
    lateinit var jenis: String
    var qty_brg: Int = 0
    var harga: Int = 0
//    lateinit var img_brg: Array<GambarBarang>

    constructor() {}
    constructor(
        id_brg:Int, nama_brg:String, deskripsi: String, jenis:String, qty_brg:Int, harga:Int,
//        img_brg: Array<GambarBarang>
    ) {
        this.id_brg = id_brg
        this.nama_brg = nama_brg
        this.deskripsi = deskripsi
        this.jenis = jenis
        this.qty_brg = qty_brg
        this.harga = harga
//        this.img_brg = img_brg
    }
}