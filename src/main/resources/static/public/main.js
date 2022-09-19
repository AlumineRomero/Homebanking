const { createApp } = Vue
createApp({
    data() {
        return {
            email: "",
            password:"",
            name:"",
            lastName:"",
            incorrect: false,
            }},
methods: {
    logIn(){
        axios.post('/api/login',"email=" + this.email + "&password=" + this.password ,{headers:{'content-type':'application/x-www-form-urlencoded'}})
        .then(response => 
        window.location.href = "/web/accounts.html"
        ).catch(response => { 
        this.incorrect = true
        console.log(this.incorrect)})
    },
    register(){
        axios.post('/api/clients',"name=" + this.name + "&lastName=" + this.lastName + "&email=" + this.email + "&password=" + this.password,{headers:{'content-type':'application/x-www-form-urlencoded'}})
        .then(response => 
            console.log("REGISTRADO")
        ).then(response => 
            this.logIn()
        )
    },
        hideReg(){
            let toggleReg = document.querySelector("#toggleReg");
            let pass = document.querySelector("#id_reg");
            
            let type = pass.getAttribute("type") === "password" ? "text" : "password";
            pass.setAttribute("type", type);
            toggleReg.classList.toggle("fa-eye");
            toggleReg.classList.toggle("fa-eye-slash");
        },
        hidePass(){
            let togglePassword = document.querySelector("#togglePassword");
            let password = document.querySelector("#id_password");
            let type = password.getAttribute("type") === "password" ? "text" : "password";       
            password.setAttribute("type", type);
            togglePassword.classList.toggle("fa-eye");          
            togglePassword.classList.toggle("fa-eye-slash");          
        }
        
        
    }
},
).mount('#app')