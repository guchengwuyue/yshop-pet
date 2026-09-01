import request from '@/config/axios'

// 商品入库明细
export interface StockinDetailVO {
  id?: number
  stockGoodsId?: number
  stockGoodsValueId?: number
  goodsName?: string // 商品名称
  spec?: string // 规格型号
  stockinNums?: number // 数量
  unitName?: string // 单位
  inboundPrice?: number // 入库单价
  amount?: number // 金额
  stockinGoodsRemark?: string // 备注
}

// 商品入库 VO
export interface StockinVO {
  id: number // ID
  docnum: string // 文号
  stockSupplierId: number // 供应商ID
  stockSupplierName?: string // 供应商名称
  supplierName?: string // 供应商名称（兼容字段）
  inboundTime: Date // 入库时间
  stockinTotalNums: number // 入库商品总数
  totalAmount: number // 单据总金额
  remark: string // 备注
  isAudit: boolean // 0-未审核 1-已=审核
  auditTime: Date // 审核时间
  isSettle: boolean // 0-未结算  1-已结算
  settleTime: Date // 结算时间
  stockStockinListList?: StockinDetailVO[] // 入库商品明细
}

// 商品入库 API
export const StockinApi = {
  // 查询商品入库分页
  getStockinPage: async (params: any) => {
    return await request.get({ url: `/stock/stockin/page`, params })
  },

  // 查询商品入库详情
  getStockin: async (id: number) => {
    return await request.get({ url: `/stock/stockin/get?id=` + id })
  },

  // 新增商品入库
  createStockin: async (data: StockinVO) => {
    return await request.post({ url: `/stock/stockin/create`, data })
  },

  // 修改商品入库
  updateStockin: async (data: StockinVO) => {
    return await request.put({ url: `/stock/stockin/update`, data })
  },

  // 删除商品入库
  deleteStockin: async (id: number) => {
    return await request.delete({ url: `/stock/stockin/delete?id=` + id })
  },

  // 导出商品入库 Excel
  exportStockin: async (params) => {
    return await request.download({ url: `/stock/stockin/export-excel`, params })
  },

  // 审核/反审核 type: audit | unaudit
  auditStockin: async (id: number, type: 'audit' | 'unaudit') => {
    return await request.get({ url: `/stock/stockin/audit?id=` + id + '&type=' + type })
  }
}