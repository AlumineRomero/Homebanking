const { createApp } = Vue
createApp({
    data() {
        return {
            client: [],
            description: "",
            amount: "",
            sourceNumber: "",
            destinationNumber: "",
            selectAccount: "",
            sourceAccount: [],
            destinationAccount: [],
            htmlEl: "",
            currentTheme: "",
            moneyFormat: new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }),
            accounts: [],
            destinationClient: {}

        }
    },
    created() {
        this.loadData()

    },
    mounted() {
        this.htmlEl = document.getElementsByTagName("html")[0];
        this.currentTheme = localStorage.getItem("theme")
            ? localStorage.getItem("theme")
            : null;
        if (this.currentTheme) {
            this.htmlEl.dataset.theme = this.currentTheme;
        }


    },
    methods: {
        loadData() {
            axios.get('/api/clients/current').then(response => {
                this.client = response.data
                this.accounts = this.client.accounts
            })
        },
        getDestinationClient() {
            axios.get('/api/transactions/' + this.destinationNumber).then(response => {
                this.destinationClient = response.data
            })
        },
        createTransaction() {
            axios.post('/api/transactions', "description=" + this.description + "&amount=" + this.amount + "&sourceNumber=" + this.sourceNumber + "&destinationNumber=" + this.destinationNumber, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    swal.fire({
                        title: '¡Transferencia realizada con éxito!',
                        icon: "success",
                        showConfirmButton: false,
                        color: 'white',
                        background: 'var(--table-transactions)',
                        showClass: {
                            popup: 'animate__animated animate__fadeInDown'
                        },
                        hideClass: {
                            popup: 'animate__animated animate__fadeOutUp'
                        }
                    })
                    setTimeout(() => {
                        window.location.href = "/web/accounts.html"
                    }, 1500)
                }).catch(response => {
                    swal({
                        title: "¡Algo salió mal!",
                        text: "Ha ocurrido un error, probá de nuevo más tarde...",
                        icon: "error",
                        button: "Ok",
                        color: 'white',
                        background: 'var(--table-transactions)',

                    });
                })
        },
        confirmTransaction() {
            let swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
            })

            swalWithBootstrapButtons.fire({
                title: '¿Estás seguro?',
                text: "Presiona 'Confirmar' para realizar la transferencia ",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: '¡Confirmar!',
                cancelButtonText: '¡Cancelar!',
                color: 'white',
                background: 'var(--table-transactions)',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    this.createTransaction()
                } else if (
                    result.dismiss === Swal.DismissReason.cancel
                ) {
                    swalWithBootstrapButtons.fire({
                        title: '¡Cancelado!',
                        icon: 'error',
                        color: 'white',
                        background: 'var(--table-transactions)',
                        showConfirmButton: false
                    })

                }
            })
        },
        darkMode(theme) {
            this.htmlEl.dataset.theme = theme
            localStorage.setItem("theme", theme)
        },
        findSourceAccount() {
            this.sourceAccount = this.accounts.filter(account => account.number == this.sourceNumber)
        },
        areYouSure() {
            let swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
            })

            swalWithBootstrapButtons.fire({
                title: '¿Estás seguro?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: '¡Cerrar sesión!',
                cancelButtonText: '¡Me quedo!',
                color: 'white',
                background: 'var(--table-transactions)',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    this.logOut()
                } else if (
                    result.dismiss === Swal.DismissReason.cancel
                ) {
                    swalWithBootstrapButtons.fire({
                        title: '¡Qué alegría!',
                        text: 'Nos alegra mucho que te quedes acá',
                        icon: 'success',
                        color: 'white',
                        background: 'var(--table-transactions)',
                        showConfirmButton: false
                    })

                }
            })
        }
    },
},
).mount('#app')