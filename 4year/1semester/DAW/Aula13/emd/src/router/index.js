import Vue from 'vue'
import VueRouter from 'vue-router'
import ListaEMD from '../views/ListaEMD.vue'
import ConsultaEMD from '../views/ConsultaEMD.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: ListaEMD
  },
  {
    path: '/emd/:id',
    name: 'consulta',
    component: ConsultaEMD
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
