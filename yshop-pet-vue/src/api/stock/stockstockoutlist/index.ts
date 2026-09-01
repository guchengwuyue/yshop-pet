import request from '@/config/axios'

// 商品出库详情 VO
export interface StockoutListVO {
  id: number // ID
  stockStockoutId: number // 出库ID
  stockGoodsValueId: number // 商品规格ID
  stockGoodsId: number // 商品ID
  stockOutNums: number // 出库单个商品数
  outboundPrice: number // 出库价格
  amount: number // 出库金额
  stockOutGoodsRemark: string // 出库商品备注
  goodsCode?: string // 商品编码
  stockGoodsName?: string // 商品名称
  stockGoodsValueName?: string // 商品规格
  unitName?: string // 单位
  docNumber?: string // 单据编号
  customerName?: string // 客户名称
  customerId?: number // 客户ID
  outboundTime?: Date // 出库时间
}

// 商品出库详情 API
export const StockoutListApi = {
  // 查询商品出库详情分页
  getStockoutListPage: async (params: any) => {
    return await request.get({ url: `/stock/stockout-list/page`, params })
  },

  // 查询商品出库详情详情
  getStockoutList: async (id: number) => {
    return await request.get({ url: `/stock/stockout-list/get?id=` + id })
  },

  // 新增商品出库详情
  createStockoutList: async (data: StockoutListVO) => {
    return await request.post({ url: `/stock/stockout-list/create`, data })
  },

  // 修改商品出库详情
  updateStockoutList: async (data: StockoutListVO) => {
    return await request.put({ url: `/stock/stockout-list/update`, data })
  },

  // 删除商品出库详情
  deleteStockoutList: async (id: number) => {
    return await request.delete({ url: `/stock/stockout-list/delete?id=` + id })
  },

  // 导出商品出库详情 Excel
  exportStockoutList: async (params) => {
    return await request.download({ url: `/stock/stockout-list/export-excel`, params })
  }
}
