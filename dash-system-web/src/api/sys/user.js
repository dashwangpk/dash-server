const user = [
  {
    name: 'userLogin',
    url: '/sys/login',
    method: 'POST'
  },
  {
    name: 'validatorRepeat',
    url: '/sys/user/validatorRepeat',
    method: 'POST'
  },
  {
    name: 'saveUpdate',
    url: '/sys/user/saveUpdate',
    method: 'POST'
  },
  {
    name: 'list',
    url: '/sys/user/list',
    method: 'POST'
  },
  {
    name: 'infos',
    url: '/sys/user/infos',
    method: 'POST'
  },
  {
    name: 'info',
    url: '/sys/user/info',
    method: 'GET'
  }, {
    name: 'resetPassword',
    url: '/sys/user/resetPassword',
    method: 'POST'
  },
  {
    name: 'delete',
    url: '/sys/user/delete',
    method: 'POST'
  },
  {
    name: 'deleteOrgUser',
    url: '/sys/orguser/deleteOrgUser',
    method: 'POST'
  },
  {
    name: 'passwordUpdate',
    url: '/sys/user/passwordUpdate',
    method: 'POST'
  },
  {
    name: 'wholeOptions',
    url: '/sys/user/wholeOptions',
    method: 'POST'
  }

]

export default user
