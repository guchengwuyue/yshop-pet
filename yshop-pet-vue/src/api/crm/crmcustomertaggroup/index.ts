import request from '@/config/axios'

// 客户标签分组 VO
export interface CustomerTagGroupVO {
  id: number // 用户ID
  groupName: string // 名称
  sort: number // 排序
}

// 客户标签分组 API
export const CustomerTagGroupApi = {
  // 查询客户标签分组分页
  getCustomerTagGroupPage: async (params: any) => {
    return await request.get({ url: `/crm/customer-tag-group/page`, params })
  },

  // 查询客户标签分组详情
  getCustomerTagGroup: async (id: number) => {
    return await request.get({ url: `/crm/customer-tag-group/get?id=` + id })
  },

  // 新增客户标签分组
  createCustomerTagGroup: async (data: CustomerTagGroupVO) => {
    return await request.post({ url: `/crm/customer-tag-group/create`, data })
  },

  asynCustomerTagGroup: async () => {
    return await request.post({ url: `/crm/customer-tag-group/sync` })
  },

  // 修改客户标签分组
  updateCustomerTagGroup: async (data: CustomerTagGroupVO) => {
    return await request.put({ url: `/crm/customer-tag-group/update`, data })
  },

  // 删除客户标签分组
  deleteCustomerTagGroup: async (id: number) => {
    return await request.delete({ url: `/crm/customer-tag-group/delete?id=` + id })
  },

  // 修改客户标签分组
  updateCustomerTagSort: async (data) => {
    return await request.put({ url: `/crm/customer-tag-group/sort`, data })
  },
}