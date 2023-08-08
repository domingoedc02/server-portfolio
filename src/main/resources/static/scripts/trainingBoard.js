
let param = window.location.search
let isSuccessfull = param.split("?update-successfull=")

if(isSuccessfull[1] === "true"){

    document.getElementById("isSuccessAlert").style.display = "block"
    
}






