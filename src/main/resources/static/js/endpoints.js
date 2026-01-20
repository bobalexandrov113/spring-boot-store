// Global variable baseUrl is set by the controller and templated by Thymeleaf

async function loginAndGetToken() {
    const outputElement = document.getElementById('output');
    if (!outputElement) {
        throw new Error("No output element found");
    }
    // const username = document.getElementById('username').value;
    // const password = document.getElementById('password').value;

    const username = 'boris.alexandrov@hotmail.ca';
    const password = '456789';

    const tokenUrl = baseUrl + "/auth/login";

    try{
        const loginResponse = await  fetch(tokenUrl, {
            method: 'POST',
            headers: new Headers({"Content-Type": "application/json"}),
            body: JSON.stringify({
                email: username,
                password: password,
            })
        });
        if (loginResponse.status === 200) {
            const authResponse = await loginResponse.json();
            const bearerToken = authResponse.token;
            outputElement.innerHTML = "logged in successfully.";
            sessionStorage.setItem('jwtToken', bearerToken);
        }
        else{
            outputElement.innerHTML = `Unable to log in successfully.${loginResponse.statusText}`;
        }

    }
    catch(error){
        console.log(error);
        outputElement.innerHTML = error;
    }
}

async function refreshToken() {
    const outputElement = document.getElementById('output');
    if (!outputElement) {
        throw new Error("No output element found");
    }
    const tokenUrl = baseUrl + "/auth/refreshToken";
    try {
        const refreshResponse = await fetch(tokenUrl, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json"
            },
        });
    }
    catch(error){
        console.log(error);
    }
    if(!refreshResponse.ok)
    {
        throw new Error("Failed to refresh token, log in please");
    }
    const authResponse = await refreshToken.json();
    const bearerToken = authResponse.token;
    outputElement.innerHTML = "refreshed token successfully.";
    console.log(bearerToken);
    sessionStorage.setItem('jwtToken', bearerToken);
}

async function getToken(){
    const outputElement = document.getElementById('output');
    if (!outputElement) {
        throw new Error("No output element found");
    }
    const bearerToken = sessionStorage.getItem('jwtToken');
    if (!bearerToken)
    {
        outputElement.innerHTML = "Log in please";
        throw new Error("No bearer token found, log in please");
    }
    let expiryDate = JSON.parse(atob(bearerToken.split('.')[1])).exp*1000;
    const isTokenExpired = Date.now() > expiryDate;
    if(isTokenExpired){
        console.log("Token expired.");
        await refreshToken();
    }
    else {
        console.log(bearerToken);
        return bearerToken;
    }
    const bearerToken1 = sessionStorage.getItem('jwtToken');
    console.log(bearerToken1);
    return bearerToken1;
}

async function getProtectedResource(protectedautUrl, bearerToken, method, CACHE_KEY){
    //check cache for the result first
    const cachedData = sessionStorage.getItem(CACHE_KEY);
    if(cachedData)
    {
        console.log("Using cached data");
        return JSON.parse(cachedData);
    }
    else
    {
        const dataResponse = await fetch(protectedautUrl, {
            method: `${method}`,
            headers: new Headers({"Accept": "application/json", "Content-Type": "application/json","Authorization": `Bearer ${bearerToken}`}),

        });
        if(dataResponse.status === 200)
        {
            const data = await dataResponse.json();
            sessionStorage.setItem(CACHE_KEY, JSON.stringify(data));
            return data;
        }
        else
        {
            throw new Error("failed to get protected resource");
        }
    }
}

function createTable(data){
    //check whether we are dealing with an array
    if(!Array.isArray(data) || data.length == 0){
        document.getElementById("data-container").innerHTML = "Failed to create table";
        return;
    }
    const headers = Object.keys(data[0]);
    let html = "<table><thead><tr>";
    headers.forEach(header => {
        html += `<th>${header}</th>`;
    });
    html += `</tr></thead><tbody>`;

    data.forEach(row => {
        html += "<tr>";
        headers.forEach(header => {
            html += `<td>${row[header]}</td>`;
        });
        html += "</tr>";
    });
    html += "</tbody></table>";
    document.getElementById("data-container").innerHTML = html;
}

/*
ResultSet should contain 2 items, id and text
 */
function createDropDownList(data, dropDownListId, containerId, outputElement){

    if(!Array.isArray(data) || data.length == 0){
        document.getElementById(containerId).innerHTML = "Failed to create a drop list";
        return;
    }
    const headers = Object.keys(data[0]);

    const container = document.getElementById(containerId);
    const dropDownList = document.createElement("md-outlined-select");
    dropDownList.id = dropDownListId;

    data.forEach(row => {
        const option = document.createElement("md-select-option");
        option.value = row[headers[0]] + "," + row[headers[1]];
        option.textContent = row[headers[1]];
        dropDownList.appendChild(option);
    });

    const handleSelectionChange = (event) => {
        const selectedValue = event.target.value;

        if (selectedValue){
            outputElement.value = selectedValue;
            console.log(selectedValue);
        }else {
            outputElement.textContent = "None";
            console.log("selection cleared.");
        }
    };
    dropDownList.addEventListener("change", handleSelectionChange);

    container.appendChild(dropDownList);



}

async function getProducts(){
    const outputElement = document.getElementById('output');
    const tableElement = document.getElementById('data-container');
    outputElement.innerHTML = '';
    tableElement.innerHTML = '';
    const bearerToken = await getToken();
    outputElement.innerHTML = 'fetching products...';
    const protectedApiUrl = baseUrl + "/products";
    const method = 'GET';
    const CACHE_KEY = 'apiProductsCache';
    try{
        const data = await getProtectedResource(protectedApiUrl, bearerToken, method, CACHE_KEY);
         createTable(data);
        //createDropDownList(data, 'productsSelect','data-container');
        outputElement.innerHTML = '<h2>Products</h2>';
    }
    catch(error){
        console.error(error);
        outputElement.innerHTML = error;
    }
}

async function getProductsInList(outputElement, tableElement){

    outputElement.innerHTML = '';
    tableElement.innerHTML = '';
    const bearerToken = await getToken();
    outputElement.innerHTML = 'fetching products...';
    const protectedApiUrl = baseUrl + "/products";
    const method = 'GET';
    const CACHE_KEY = 'apiProductsCache';
    try{
        const data = await getProtectedResource(protectedApiUrl, bearerToken, method, CACHE_KEY);

        createDropDownList(data, 'productsSelect','data-container',outputElement);
        outputElement.innerHTML = '<h2>Products</h2>';
    }
    catch(error){
        console.error(error);
        outputElement.innerHTML = error;
    }
}

async function getShoppingPage  (){

    const tableElement = document.getElementById('data-container');
    tableElement.innerHTML = '';


    const container = document.createElement('div');
    container.classList.add('container');
    tableElement.appendChild(container);

   const leftDiv = document.createElement('div');
   leftDiv.id = 'lp';
   leftDiv.classList.add('left-panel');
   const outputElement = document.createElement('md-outlined-text-field');
   outputElement.id='output_data';
   outputElement.label="product";
   outputElement.textContent = 'new Item';
   leftDiv.appendChild(outputElement);
   container.appendChild(leftDiv);

    const rightDiv = document.createElement('div');
    rightDiv.id = 'rp';
    rightDiv.classList.add('right-panel');
    await getProductsInList(outputElement, rightDiv );
    const  dropDownList = document.getElementById('productsSelect');
    rightDiv.appendChild(dropDownList);

    container.appendChild(rightDiv);
    
    






}