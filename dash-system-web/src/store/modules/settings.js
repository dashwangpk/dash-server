import defaultSettings from '@/settings'
const { showSettings, tagsView, fixedHeader, sidebarLogo, theme } = defaultSettings

const state = {
  theme: theme,
  showSettings: showSettings,
  tagsView: tagsView,
  fixedHeader: fixedHeader,
  sidebarLogo: sidebarLogo,
  showRightPanel: false
}

const mutations = {
  CHANGE_SETTING: (state, { key, value }) => {
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  },
  SET_SHOWRIGHTPANEL: (state, showRightPanel) => {
    state.showRightPanel = showRightPanel
  }
}

const actions = {
  changeSetting({ commit }, data) {
    commit('CHANGE_SETTING', data)
  },
  setShowRightPanel({ commit }, showRightPanel) {
    commit('SET_SHOWRIGHTPANEL', showRightPanel)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

