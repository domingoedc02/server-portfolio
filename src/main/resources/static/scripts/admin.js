let param = window.location.search
let isSuccessfull = param.split("?")

console.log(isSuccessfull)

if(isSuccessfull[1] === "user-added-successfull=true"){
    let userId = isSuccessfull[2].split("=")
    document.getElementById("memberId").innerHTML = "メンバーID: "+userId[1]
    document.getElementById("success").style.display = "block"
} else if (isSuccessfull[1] === "user-added-successfull=false"){
    let error = isSuccessfull[2].split("=")
    if(error[1] === "invalid-user"){
        document.getElementById("invalid").style.display = "block"
    } else if(error[1] === "database-error"){
        document.getElementById("databaseError").style.display = "block"
    }
} else if(isSuccessfull[1] === "trainingadd-successfull=true"){
    let trainingId = isSuccessfull[2].split("=")
    document.getElementById("trainingId").innerHTML = "研修ID: "+trainingId[1]
    document.getElementById("successTraining").style.display = "block"
} else if(isSuccessfull[1] === "trainingadd-successfull=false"){
    document.getElementById("invalidTraining").style.display = "block"
}
