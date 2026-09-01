import request from '@/config/axios'

// 供应商 VO
export interface SupplierVO {
  id: number // id
  name: string // 名称
  shortName: string // 短名
  code: string // 代码
  shipAddress: string // 发货地址
  address: string // 联系地址
  contacts: string // 联系人
  telphone: string // 联系电话
  remark: string // 备注
}

// 供应商 API
export const SupplierApi = {
  // 查询供应商分页
  getSupplierPage: async (params: any) => {
    return await request.get({ url: `/stock/supplier/page`, params })
  },

  // 查询供应商详情
  getSupplier: async (id: number) => {
    return await request.get({ url: `/stock/supplier/get?id=` + id })
  },

  // 新增供应商
  createSupplier: async (data: SupplierVO) => {
    return await request.post({ url: `/stock/supplier/create`, data })
  },

  // 修改供应商
  updateSupplier: async (data: SupplierVO) => {
    return await request.put({ url: `/stock/supplier/update`, data })
  },

  // 删除供应商
  deleteSupplier: async (id: number) => {
    return await request.delete({ url: `/stock/supplier/delete?id=` + id })
  },

  // 导出供应商 Excel
  exportSupplier: async (params) => {
    return await request.download({ url: `/stock/supplier/export-excel`, params })
  }
}