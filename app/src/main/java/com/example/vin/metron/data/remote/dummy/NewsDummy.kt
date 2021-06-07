package com.example.vin.metron.data.remote.dummy

object NewsDummy {
    private val listDummyNews = ArrayList<News>()

    fun getDummyNews(): ArrayList<News> {
        listDummyNews.add(
            News(
                title = "Perjalanan Diskon Listrik yang Tak Ada Lagi Mulai Bulan Depan",
                detail = "Jakarta - Diskon tarif listrik untuk golongan 450 VA dan 900 VA bersubsidi tak ada lagi mulai Juli 2021. Pemerintah memutuskan untuk tidak lagi memperpanjangnya. Bagaimana perjalanannya?",
                url = "https://finance.detik.com/energi/d-5594277/perjalanan-diskon-listrik-yang-tak-ada-lagi-mulai-bulan-depan?_ga=2.12927157.340619384.1622962702-1649740446.1618321563",
                photo = "https://akcdn.detik.net.id/community/media/visual/2020/06/30/momen-pln-terjun-langsung-cek-meteran-warga-5_169.jpeg?w=700&q=90",
                date = "Sabtu,05 Juni 2021"
            )
        )
        listDummyNews.add(
            News(
                title = "Utang Jumbo PLN: Rp 500 Triliun!",
                detail = "Jakarta - PT PLN (Persero) punya utang yang sangat besar, yakni mencapai Rp 500 triliun. Menteri BUMN Erick Thohir mengatakan, salah satu cara yang dilakukan untuk membenahi keuangan PLN ialah menekan 50% belanja modal (capital expenditure/capex)...",
                url = "https://finance.detik.com/berita-ekonomi-bisnis/d-5594703/utang-jumbo-pln-rp-500-triliun?_ga=2.12927157.340619384.1622962702-1649740446.1618321563",
                photo = "https://akcdn.detik.net.id/community/media/visual/2015/12/17/261d026a-5d4f-4c75-8457-55066eefc967_169.jpg?w=700&q=90",
                date = "Sabtu, 5 Juni 2021"
            )
        )
        listDummyNews.add(
            News(
                title = "Ombudsman Sumut Bakal Panggil Dirut PDAM Tirtanadi soal Lonjakan Tagihan Air",
                detail = "Jakarta - Ombudsman RI Perwakilan Sumatera Utara (Sumut) telah menerima 39 laporan warga soal lonjakan tagihan air PDAM Tirtanadi yang dianggap tidak wajar. Ombudsman Sumut bakal memanggil pimpinan perusahaan air itu untuk dimintai klarifikasinya...",
                url = "https://news.detik.com/berita/d-5497525/ombudsman-sumut-bakal-panggil-dirut-pdam-tirtanadi-soal-lonjakan-tagihan-air?_ga=2.54029482.340619384.1622962702-1649740446.1618321563",
                photo = "https://akcdn.detik.net.id/community/media/visual/2021/03/12/warga-medan-lapor-ke-ombudsman-perihal-tagihan-air-yang-melonjak_169.jpeg?w=700&q=90",
                date = "Rabu, 17 Maret 2021"
            )
        )
        listDummyNews.add(
            News(
                title = "Pelanggan PDAM di Gresik Keluhkan Distribusi Air Tak Lancar",
                detail = "Gresik - Pelanggan PDAM di Gresik mengeluhkan distribusi air yang tak lancar. Untuk memenuhi kebutuhan air bersih, mereka harus membeli sebab hampir satu bulan, saluran air PDAM ke rumahnya tidak lancar.",
                url =
                "https://akcdn.detik.net.id/community/media/visual/2020/06/30/momen-pln-terjun-langsung-cek-meteran-warga-5_169.jpeg?w=700&q=90",
                photo = "https://akcdn.detik.net.id/community/media/visual/2020/08/24/ilustrasi-air_43.jpeg?w=700&q=90",
                date = "Jumat, 19 Feb 2021"
            )
        )
        return listDummyNews
    }
}