<template>
  <div>
    <p v-if="!emdReady">A carregar exame...</p>
    <Consulta v-else :e="emd"/>
  </div>
</template>

<script>
import Consulta from '@/components/Consulta'
import axios from "axios"

const host = require("@/config/hosts").host

export default {
  name: 'consultaEMD',
  components: {
    Consulta
  },

  data: () => ({
    emd: [],
    emdReady: false
  }),

  created: async function() {
    try{
      let response = await axios.get(host + "/emds?_id.$oid=" + this.$route.params.id)
      this.emd = response.data[0]
      this.emdReady = true
    } catch (e){
      return e
    }
  }
}

</script>