let param = window.location.search
let isSuccessfull = param.split("?reset-password=")

if(isSuccessfull[1] === "true"){
    document.getElementById("isSuccessAlert").style.display = "block"
}