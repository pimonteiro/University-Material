<template>
    <v-card class="ma-8">
        <v-card-title class="indigo darken-4 white--text" dark>
            Consulta de Exame Médico Desportivo
        </v-card-title>

        <v-card-text>
            <p>{{ e._id.$oid }}</p>
            <p>{{ e.nome }}</p>
        </v-card-text>
    </v-card>
</template>

<script>
export default {
    props: ["e"]
}
</script>