import request from '@/config/axios'

// 商品出库明细
export interface StockoutDetailVO {
  id?: number
  stockGoodsId?: number
  stockGoodsValueId?: number
  goodsName?: string // 商品名称
  spec?: string // 规格型号
  stockOutNums?: number // 数量
  unitName?: string // 单位
  outboundPrice?: number // 出库单价
  amount?: number // 金额
  stockOutGoodsRemark?: string // 备注
}

// 商品出库 VO
export interface StockoutVO {
  id: number // ID
  docnum: string // 出库单编号
  outboundTime: Date // 出库时间
  stockCustomerId: number // 客户ID
  stockCustomerName?: string // 客户名称
  stockOutTotalNums: number // 出库总商品数
  totalOutAmount: number // 出库总金额
  remark: string // 备注
  isAudit: boolean // 0-未审核 1-已=审核
  auditTime: Date // 审核时间
  stockStockoutListList?: StockoutDetailVO[] // 出库商品明细
  stockoutLists?: StockoutDetailVO[] // 提交用出库明细
}

// 商品出库 API
export const StockoutApi = {
  // 查询商品出库分页
  getStockoutPage: async (params: any) => {
    return await request.get({ url: `/stock/stockout/page`, params })
  },

  // 查询商品出库详情
  getStockout: async (id: number) => {
    return await request.get({ url: `/stock/stockout/get?id=` + id })
  },

  // 新增商品出库
  createStockout: async (data: StockoutVO) => {
    return await request.post({ url: `/stock/stockout/create`, data })
  },

  // 修改商品出库
  updateStockout: async (data: StockoutVO) => {
    return await request.put({ url: `/stock/stockout/update`, data })
  },

  // 删除商品出库
  deleteStockout: async (id: number) => {
    return await request.delete({ url: `/stock/stockout/delete?id=` + id })
  },

  // 导出商品出库 Excel
  exportStockout: async (params) => {
    return await request.download({ url: `/stock/stockout/export-excel`, params })
  },

  // 审核/反审核 type: audit | unaudit
  auditStockout: async (id: number, type: 'audit' | 'unaudit') => {
    return await request.get({ url: `/stock/stockout/audit?id=` + id + '&type=' + type })
  }
}
