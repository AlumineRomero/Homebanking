const { createApp } = Vue
createApp({
    data() {
        return {
            clients: [],
            clientName: "",
            clientLastName: "",
            clientEmail: "",
            data: "",
            loans: [],
            dateFormat: new Intl.DateTimeFormat('es-AR', { month: "numeric", year: "2-digit" }),
            moneyFormat: new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }),
            percentFormat : new Intl.NumberFormat('en-US', {style: 'percent', maximumFractionDigits:2}),
            }},
    created() {
        this.loadData()
        this.loadLoans()
    },
methods: {
    loadData(){
        axios.get('/api/clients').then(response =>{
            this.clients = response.data
        })
    },
    loadLoans(){
        axios.get('/api/loans').then(response =>{
            this.loans = response.data
        })
    },
    
    }
},
).mount('#app')