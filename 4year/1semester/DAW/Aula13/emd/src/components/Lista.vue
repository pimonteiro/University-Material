<template>
  <v-card class="ma-8">
    <v-card-title class="indigo darken-4 white--text" dark>
      Lista de Exames Médicos Desportivos
    </v-card-title>

    <v-card-text>
      <v-data-table
        :headers="headers"
        :items="mylista"
        class="elevation-1"
        :footer-props="footer_props"
      >
        <template v-slot:item="props">
          <tr>
            <td v-for="(campo, index) in props.item" v-bind:key="index">
              {{ campo }}
            </td>
            <td>
              <v-icon
                @click="showExame(props.item.id)"
                color="indigo darken-2"
                >visibility
              </v-icon>
            </td>
          </tr>
        </template>
        <template v-slot:pageText="props">
          Resultados: {{ props.pageStart }} - {{ props.pageStop }} de
          {{ props.itemsLength }}
        </template>
      </v-data-table>
    </v-card-text>
  </v-card>
</template>

<script>
export default {
  name: 'Lista',
  props: ["lista"],

  data: () => ({
    headers: [
      {text: "Identificador", value: "id"},
      {text: "Data", value: "dataExame"},
      {text: "Nome", value: "nome"},
      {text: "Modalidade", value: "modalidade"},
      {text: "Resultado", value: "resultadoEMD"},
      {text: "Pagamento", value: "pagamento"},
      {text: "Operações"}      
    ],
    mylista: [],
    footer_props: {
      "items-per-page-options": [3, 5, -1],
      "items-per-page-text": "Mostrar"
    }
  }),

  created: function(){
    this.mylista = this.lista.map(item => {
      return {
        id: item._id.$oid,
        dataExame: item.dataExame,
        nome: item.nome,
        modalidade: item.modalidade,
        resultadoEMD: item.resultadoEMD,
        pagamento: item.pagamento
      }
    })
  },

  methods: {
    showExame: function(i) {
      this.$router.push("/emd/" + i)
    }
  }
}
</script>
