// Global variable baseUrl is set by the controller and templated by Thymeleaf
let numCartItems = 0;

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

function createDropDownList(data, dropDownListId, containerId){

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
        option.value = row[headers[0]];
        option.textContent = row[headers[1]];
        dropDownList.appendChild(option);
    });

    const  handleSelectionChange = async(event) => {
        const selectedValue = event.target.value;

        if (selectedValue){
            await getProduct(selectedValue);
            console.log(selectedValue);
        }else {

            console.log("selection cleared.");
        }
    };
    dropDownList.addEventListener("change", handleSelectionChange);

    container.appendChild(dropDownList);
}

async function getProductsInList( container){

    container.innerHTML = '';
    const bearerToken = await getToken();
    const protectedApiUrl = baseUrl + "/products";
    const method = 'GET';
    const CACHE_KEY = 'apiProductsCache';
    try{
        const data = await getProtectedResource(protectedApiUrl, bearerToken, method, CACHE_KEY);

        createDropDownList(data, 'productsSelect','data-container');

    }
    catch(error){
        console.error(error);
        container.innerHTML = error;
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
    // leftDiv.classList.add('left-panel');
    await getProductsInList(leftDiv );
    const  dropDownList = document.getElementById('productsSelect');
    leftDiv.appendChild(dropDownList);
    container.appendChild(leftDiv);

    const rightDiv = document.createElement('div');
    rightDiv.id = 'rp';
    // rightDiv.classList.add('right-panel');

    const productField = document.createElement('md-outlined-text-field');
    productField.id='product_name';
    productField.label="product";

    rightDiv.appendChild(productField);
    container.appendChild(rightDiv);

    const productPriceField = document.createElement('md-outlined-text-field');
    productPriceField.id='product_price';
    productPriceField.label="product price";
    rightDiv.appendChild(productPriceField);
    container.appendChild(rightDiv);

    const productDescriptionField = document.createElement('md-outlined-text-field');
    productDescriptionField.id='product_description';
    productDescriptionField.label="product description";
    productDescriptionField.type="textarea";
    productDescriptionField.rows='10';
    productDescriptionField.resize='vertical';
    rightDiv.appendChild(productDescriptionField);

    const addItemButton = document.createElement('md-elevated-button');
    addItemButton.id = 'addItemButton';

    const  handleClickCartButton = async(event) => {
            await addItemToCart();
            console.log("Click on Item Button");
    };

    addItemButton.addEventListener('click', handleClickCartButton);
    addItemButton.textContent = 'Add Item';

    rightDiv.appendChild(addItemButton);

    container.appendChild(rightDiv);



    const cartDiv = document.createElement('div');
    cartDiv.id = 'cartDiv';
    cartDiv.classList.add('cbox');

    const cartField = document.createElement('md-outlined-text-field');
    cartField.id='cartField';
    cartField.label="cart";
    cartField.value = 'no items in cart';



    cartDiv.appendChild(cartField);



    const cartItemList = document.createElement('md-list');







    cartItemList.id = 'cartItemList';

    cartListItem1 = document.createElement('md-list-item');
    cartListItem1.textContent = 'Items';
    cartItemList.appendChild(cartListItem1);
    divider = document.createElement('md-divider');
    cartItemList.appendChild(divider);

    cartDiv.appendChild(cartItemList);
    container.appendChild(cartDiv);


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

async function getProduct( productId){
    const product = document.getElementById('product_name');
    const productDescription = document.getElementById('product_description');
    const productPrice = document.getElementById('product_price');


    const bearerToken = await getToken();
    const protectedApiUrl = baseUrl + "/products/" + productId;
    const method = 'GET';
    const CACHE_KEY = 'apiProductCache' + productId;
    try{
        const data = await getProtectedResource(protectedApiUrl, bearerToken, method, CACHE_KEY);
        product.value = data['name'];
        productDescription.value = data['description'];
        productPrice.value = data['price'];
        console.log(data);

    }
    catch(error){
        console.error(error);
        productDescription.value = error;
        productPrice.value = 'error';
        product.value= 'error';
    }
}

async function addItemToCart(){
    const productId = document.getElementById('productsSelect').value;
    const cartItemList = document.getElementById('cartItemList');
    const cartField = document.getElementById('cartField');


    if(numCartItems == 0)
    {
        //create a new cart, store the values in the session storage
        const cartApiUrl = baseUrl + "/carts";
        let dummydata = 'nonsense';

        const data = await postData(cartApiUrl, dummydata);
        sessionStorage.setItem('CART_ID', data['id']);
        console.log(data['id']);

    }
    const cartId = sessionStorage.getItem('CART_ID');
    const url =  baseUrl + "/carts/" + cartId + "/items";
    const productInfo = { productId:productId };
    const dumdum = await postData(url, productInfo);
    if(dumdum)
    {
       console.log(dumdum);
       const cartUrl =  baseUrl + "/carts/" + cartId;
       const data = await getData(cartUrl);

       console.log("***********  cart data returned for " + data.id);
       cartField.value = "Total: " + data.totalPrice;
       cartItemList.innerHTML = '';
       // add header
        const cartListHeader = document.createElement('md-list-item');

        cartListHeader.textContent = "Cart items";
        cartItemList.appendChild(cartListHeader);
        //add divider
        divider = document.createElement('md-divider');
        cartItemList.appendChild(divider);

        const  handleClickCartList = async(event) => {
            const item = event.target;
            if(item)
            {
                const productId = item.id;
                removeItemFromCart(productId);
                console.log("removing product id ....  " + productId);


            }
            else
            {
                console.log("failed to get the item from cart");
            }
            console.log("Click on the cart list");
        };

       const items = data.items;
       items.forEach(item => {
           const record = item.product.name + ":" + item.quantity;
           console.log(record);
           const cartListItem = document.createElement('md-list-item');
           cartListItem.id = item.product.id;
           cartListItem.textContent = record;
           cartListItem.addEventListener('click', handleClickCartList);
           cartItemList.appendChild(cartListItem);
       })


    }




    numCartItems++;
    //cartField.value = "items: " + numCartItems;
    //console.log(" async function value of " + productId);
}

async function removeItemFromCart(productId){
        console.log("removing " + productId);

}

async function postData(url, data){
    try{
        const dataResponse = await fetch(url,{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });
        if(dataResponse.ok)
        {
            const data = await dataResponse.json();
            console.log(data);
            return data;
        }

    }catch(error){
        console.error('Post data error:',error);
    }
}

async function getData(url){
    try{
        const dataResponse = await fetch(url,{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },

        });
        if(dataResponse.ok)
        {
            const data = await dataResponse.json();
            console.log(data);
            return data;
        }

    }catch(error){
        console.error('Post data error:',error);
    }
}