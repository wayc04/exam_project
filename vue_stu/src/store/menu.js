const state = {
    isCollapse: false,
    selectMenu: [
        
    ]
}

const mutations = {
    collapseMenu (state) {
        state.isCollapse = !state.isCollapse
    },
    addMenu(state, payload) {
        if (state.selectMenu.findIndex(item => item.path === payload.path) === -1) {
            state.selectMenu.push(payload)
        }
    },
    closeMenu(state, payload) {
        const index = state.selectMenu.findIndex(val => val.name === payload.name)
        state.selectMenu.splice(index, 1)
    },
    clearMenu(state) {
        state.selectMenu = []
    }
}


export default{
    state,
    mutations
}