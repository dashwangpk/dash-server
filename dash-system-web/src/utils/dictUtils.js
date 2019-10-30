import api from '@/api'

export function dictOptions(aaa100) {
  let typeOptions = {}
  // aaa100 编码
  api.cfgAa10.wholeoptions({ data: { aaa100: aaa100 }}).then(res => {
    if (res && res.data && res.data.code === 0) {
      const tempTypeOptions = {}
      res.data.wholeoptions.forEach((item, index, array) => {
        tempTypeOptions[item.aaa102] = item.aaa103
      })
      typeOptions = tempTypeOptions
    }
  })
  return typeOptions
}

