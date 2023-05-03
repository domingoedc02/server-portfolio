function login(){
    // console.log('hello')
    let email =  document.getElementById('Email').value
    let password =  document.getElementById('Password').value

    fetch('http://localhost:8090/api/v1/auth/authenticate', {
        method: 'POST',
        headers:{
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            username: email,
            password: password
        })
    })
    .then((response) => response.json())
    .then((result) => {
        if(result.token !== undefined){
            document.getElementById('invalid').style.display = "none"
            localStorage.setItem('token', result.token)
            // window.location.replace('/users?token='+result.token)
            console.log("logged in")
        } else{
            // document.getElementById('invalid').style.display = "block"
        }
        console.log(result.error)
        // localStorage.setItem('token', result.token)
        // document.getElementById('invalid').style.display = "none"
        // window.location.href = "/pages/home.html"
    })
    .catch((error) => {
        // console.error('Error:', error);
        document.getElementById('invalid').style.display = "block"
        
    });
}