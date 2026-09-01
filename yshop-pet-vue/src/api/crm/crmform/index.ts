import type { FormGroup, FormModule } from '@/views/crm/crmform/types'
import { getDefaultCluesFormConfig } from '@/views/crm/crmform/mock/cluesForm'
import { getDefaultCustomerFormConfig } from '@/views/crm/crmform/mock/customerForm'
import request from '@/config/axios'

const STORAGE_PREFIX = 'crm_form_'

function storageKey(module: FormModule): string {
  return `${STORAGE_PREFIX}${module}`
}

function getDefaultConfig(module: FormModule): FormGroup[] {
  if (module === 'customer') return getDefaultCustomerFormConfig()
  return getDefaultCluesFormConfig()
}

/**
 * 自定义表单配置 API
 */
export const CrmFormApi = {
  /** 获取表单配置 */
  async getFormConfig(module: FormModule = 'clues'): Promise<FormGroup[]> {
    try {
      const res = await request.get({ url: `/crm/form/get`, params: { module } })
      if (res?.formGroupJson) {
        return JSON.parse(res.formGroupJson)
      }
    } catch {
      // fall through to default
    }
    return getDefaultConfig(module)
  },

  /** 保存表单配置 */
  async saveFormConfig(module: FormModule, groups: FormGroup[]): Promise<boolean> {
    return await request.post({
      url: `/crm/form/create`,
      data: { formModule: module, formGroupJson: JSON.stringify(groups) }
    })
  },

  /** 重置为默认配置并保存 */
  async resetFormConfig(module: FormModule = 'clues'): Promise<FormGroup[]> {
    localStorage.removeItem(storageKey(module))
    const defaults = getDefaultConfig(module)
    await CrmFormApi.saveFormConfig(module, defaults)
    return defaults
  }
}
