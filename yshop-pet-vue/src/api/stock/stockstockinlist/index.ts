import request from '@/config/axios'

// 商品入库详情 VO
export interface StockinListVO {
  id: number // id
  stockStockinId: number // 入库主表ID
  stockGoodsId: number // 商品ID
  stockGoodsValueId: number // 商品规格id
  stockinNums: number // 入库数量
  inboundPrice: number // 入库价格
  amount: number // 入库金额
  stockinGoodsRemark: string // 入库备注
  charged: number // 已出库:0=否,1=是
}

// 商品入库详情 API
export const StockinListApi = {
  // 查询商品入库详情分页
  getStockinListPage: async (params: any) => {
    return await request.get({ url: `/stock/stockin-list/page`, params })
  },

  // 查询商品入库详情详情
  getStockinList: async (id: number) => {
    return await request.get({ url: `/stock/stockin-list/get?id=` + id })
  },

  // 新增商品入库详情
  createStockinList: async (data: StockinListVO) => {
    return await request.post({ url: `/stock/stockin-list/create`, data })
  },

  // 修改商品入库详情
  updateStockinList: async (data: StockinListVO) => {
    return await request.put({ url: `/stock/stockin-list/update`, data })
  },

  // 删除商品入库详情
  deleteStockinList: async (id: number) => {
    return await request.delete({ url: `/stock/stockin-list/delete?id=` + id })
  },

  // 导出商品入库详情 Excel
  exportStockinList: async (params) => {
    return await request.download({ url: `/stock/stockin-list/export-excel`, params })
  }
}