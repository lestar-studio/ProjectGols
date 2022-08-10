package com.example.projectgols.model

class Pesanan {
    var id_pesanan: String = ""
    var id_user: String = ""
    var tgl_get: String = ""
    var status: String = ""
    var detail: ArrayList<DetailPesanan> = arrayListOf()

    constructor() {}
    constructor(
        id_pesanan:String, id_user:String, tgl_get: String, status: String, detail: ArrayList<DetailPesanan>
    ) {
        this.id_pesanan = id_pesanan
        this.id_user = id_user
        this.tgl_get = tgl_get
        this.status = status
        this.detail = detail
    }
}