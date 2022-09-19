const { createApp } = Vue
createApp({
    data() {
        return {
            client: [],
            accounts: [],
            loans: [],
            id: "",
            amount: "",
            accountNumber: "",
            payments: "",
            optionPayments: [],
            selectedLoan: [],
            totalBalance: 0,
            htmlEl: "",
            currentTheme: "",
            valuePayment: "",
            dateFormat: new Intl.DateTimeFormat('es-AR', { month: "numeric", year: "2-digit" }),
            moneyFormat: new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }),
            percentFormat : new Intl.NumberFormat('en-US', {style: 'percent', maximumFractionDigits:2}),
        }
    },
    created() {
        this.loadData()
        this.loadLoans()
        console.log(this.loanApproved)
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
        loadLoans() {
            axios.get('/api/loans').then(response => {
                this.loans = response.data
            })
        },
        findRequestLoan() {
            this.selectedLoan = this.loans.filter(loan => loan.id == this.id)
            this.optionPayments = this.selectedLoan[0].payments
            console.log(this.selectedLoan[0].interest)
        },
        requestLoan() {
            axios.post("/api/loans", { id: this.id, amount: this.amount, payments: this.payments, destinationNumberAccount: this.accountNumber })
                .then(response => {
                    swal.fire({
                        title: '¡Préstamo aprobado!',
                        text: 'En instantes acreditaremos el monto solicitado a la cuenta seleccionada',
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
                    swal.fire({
                        title: "¡Algo salió mal!",
                        text: "Ha ocurrido un error, probá de nuevo más tarde...",
                        icon: "error",
                        button: "Ok",
                        color: 'white',
                        background: 'var(--table-transactions)'
                    });
                })

        },
        darkMode(theme) {
            this.htmlEl.dataset.theme = theme
            localStorage.setItem("theme", theme)
        },
        logOut() {
            axios.post('/api/logout').then(response =>
                window.location.href = "/public/index.html"
            )
        },
        paymentValue() {
            this.valuePayment = (this.amount * this.selectedLoan[0].interest + this.amount) / this.payments
            console.log(this.valuePayment)
        },
        confirmLoanRequest() {
            let swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
            })

            swalWithBootstrapButtons.fire({
                title: '¿Estás seguro?',
                text: "Una vez acreditado el monto solicitado no podés cancelar el préstamo...",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: '¡Confirmar!',
                cancelButtonText: '¡Cancelar!',
                color: 'white',
                background: 'var(--table-transactions)',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    this.requestLoan()
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



    }
},
).mount('#app')
