<template>
  <div>
    <p v-if="!emdsReady">A carrregar exames...</p>
    <Lista v-else :lista="emds" />
  </div>
</template>

<script>
import Lista from '@/components/Lista'
import axios from "axios"

const host = require("@/config/hosts").host

export default {
  name: 'listaEMD',
  components: {
    Lista
  },

  data: () => ({
    emds: [],
    emdsReady: false
  }),

  created: async function() {
    try{
      let response = await axios.get(host + "/emds")
      this.emds = response.data
      this.emdsReady = true
    } catch (e){
      return e
    }
  }
}

</script>
