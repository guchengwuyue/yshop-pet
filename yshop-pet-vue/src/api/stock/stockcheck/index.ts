import request from '@/config/axios'

/** 商品盘库明细 */
export interface StockCheckDetailVO {
  id?: number
  stockGoodsId?: number
  stockGoodsValueId?: number
  goodsName?: string
  stockGoodsName?: string
  spec?: string
  stockGoodsValueName?: string
  goodsCode?: string
  unitName?: string
  bookNums?: number // 账面数量
  actualNums?: number // 实盘数量
  diffNums?: number // 盈亏
  checkGoodsRemark?: string
}

/** 商品盘库 VO */
export interface StockCheckVO {
  id: number
  docnum: string // 单据编号
  checkTime: Date // 盘库时间
  checkTotalNums: number // 盘点商品总数
  profitNums: number // 盘盈合计
  lossNums: number // 盘亏合计
  remark: string // 备注
  isAudit: boolean | number // 0-未审核 1-已审核
  auditTime: Date // 审核时间
  stockStockcheckListList?: StockCheckDetailVO[]
  checkLists?: StockCheckDetailVO[]
}

/** 商品盘库 API */
export const StockCheckApi = {
  // 查询商品盘库分页
  getStockCheckPage: async (params: any) => {
    return await request.get({ url: `/stock/stockcheck/page`, params })
  },

  // 查询商品盘库详情
  getStockCheck: async (id: number) => {
    return await request.get({ url: `/stock/stockcheck/get?id=` + id })
  },

  // 新增商品盘库
  createStockCheck: async (data: StockCheckVO) => {
    return await request.post({ url: `/stock/stockcheck/create`, data })
  },

  // 修改商品盘库
  updateStockCheck: async (data: StockCheckVO) => {
    return await request.put({ url: `/stock/stockcheck/update`, data })
  },

  // 删除商品盘库
  deleteStockCheck: async (id: number) => {
    return await request.delete({ url: `/stock/stockcheck/delete?id=` + id })
  },

  // 审核/反审核 type: audit | unaudit
  auditStockCheck: async (id: number, type: 'audit' | 'unaudit') => {
    return await request.get({ url: `/stock/stockcheck/audit?id=` + id + '&type=' + type })
  }
}
