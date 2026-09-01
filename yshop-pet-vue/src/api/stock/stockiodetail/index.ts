import request from '@/config/axios'

/** 商品出入盘库明细 VO */
export interface StockIoDetailVO {
  rowKey: string
  id: number
  ioType: string
  docnum?: string | null
  stockGoodsId: number
  stockGoodsValueId: number
  goodsCode: string
  stockGoodsName: string
  stockGoodsValueName: string
  unitName: string
  unitPrice?: number | null
  stockinNums?: number | null
  stockinAmount?: number | null
  stockOutNums?: number | null
  stockOutAmount?: number | null
  bookNums?: number | null
  actualNums?: number | null
  diffNums?: number | null
  ioTime: string
}

/** 商品出入盘库明细 API */
export const StockIoDetailApi = {
  /** 分页查询商品出入盘库明细（入库+出库+盘库合并，按出入盘时间倒序） */
  getStockIoDetailPage: async (params: any) => {
    return await request.get({ url: `/stock/stock-io-detail/page`, params })
  }
}
