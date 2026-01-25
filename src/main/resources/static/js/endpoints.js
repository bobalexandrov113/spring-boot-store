// Global variable baseUrl is set by the controller and templated by Thymeleaf
let numCartItems = 0;

async function loginAndGetToken() {
    const outputElement = document.getElementById('output');
    if (!outputElement) {
        throw new Error("No output element found");
    }
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;



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
        // console.log(error);
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
        // console.log(error);
    }
    if(!refreshResponse.ok)
    {
        throw new Error("Failed to refresh token, log in please");
    }
    const authResponse = await refreshToken.json();
    const bearerToken = authResponse.token;
    outputElement.innerHTML = "refreshed token successfully.";
    // console.log(bearerToken);
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
        // console.log("Token expired.");
        await refreshToken();
    }
    else {
        // console.log(bearerToken);
        return bearerToken;
    }
    const bearerToken1 = sessionStorage.getItem('jwtToken');
    // console.log(bearerToken1);
    return bearerToken1;
}

async function getProtectedResource(protectedautUrl, bearerToken, method, CACHE_KEY){
    //check cache for the result first
    const cachedData = sessionStorage.getItem(CACHE_KEY);
    if(cachedData)
    {
        // console.log("Using cached data");
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



async function postProtectedResource(protectedautUrl, bearerToken, data){

       console.log(protectedautUrl);
       console.log(bearerToken);
       console.log(JSON.stringify(data));
        const dataResponse = await fetch(protectedautUrl, {
            method: 'POST',
            headers: new Headers({"Accept": "application/json", "Content-Type": "application/json","Authorization": `Bearer ${bearerToken}`}),
            body: JSON.stringify(data),
        });
        if(dataResponse.ok)
        {
            const response = await dataResponse.json();
            console.log(response.checkoutUrl);
            return response;
        }
        else
        {
            throw new Error("failed to POST protected resource");
        }

}




function createTable(data, header){
    //check whether we are dealing with an array
    if(!Array.isArray(data) || data.length == 0){
        document.getElementById("data-container").innerHTML = "Failed to create table";
        return;
    }
    const headers = Object.keys(data[0]);
    let html = "<h2>"+header+"</h2><table><thead><tr>";
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
            // console.log(selectedValue);
        }else {

            // console.log("selection cleared.");
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

    const dataContainer = document.getElementById('data-container');
    dataContainer.innerHTML = '';


    const container = document.createElement('div');
    container.classList.add('container');
    dataContainer.appendChild(container);

   const leftDiv = document.createElement('div');

    leftDiv.id = 'lp';


    await getProductsInList(leftDiv );
    const  dropDownList = document.getElementById('productsSelect');
    dropDownList.label = "select product from list";
    leftDiv.appendChild(dropDownList);
    container.appendChild(leftDiv);



    const rightDiv = document.createElement('div');
    rightDiv.id = 'rp';

    const verticalContainer = document.createElement('div');
    verticalContainer.classList.add('distibuted-stack');

    const headerDiv = document.createElement('div');
    headerDiv.innerHTML="<center><h3>Product Details</h3></center>";
    verticalContainer.appendChild(headerDiv);

    const firstRow = document.createElement('div');
    const productField = document.createElement('md-outlined-text-field');
    productField.id='product_name';
    productField.label="product";

    firstRow.appendChild(productField);



    const productPriceField = document.createElement('md-outlined-text-field');
    productPriceField.id='product_price';
    productPriceField.label="product price";
    firstRow.appendChild(productPriceField);
    verticalContainer.appendChild(firstRow);
    rightDiv.appendChild(verticalContainer);


    const secondRow = document.createElement('div');

    const productDescriptionField = document.createElement('md-outlined-text-field');
    productDescriptionField.id='product_description';
    productDescriptionField.label="product description";
    productDescriptionField.type="textarea";
    productDescriptionField.rows='10';
    productDescriptionField.resize='vertical';
    secondRow.appendChild(productDescriptionField);

    const addItemButton = document.createElement('md-elevated-button');
    addItemButton.id = 'addItemButton';



    const  handleClickCartButton = async(event) => {
            await addItemToCart();
            // console.log("Click on Item Button");
    };

    addItemButton.addEventListener('click', handleClickCartButton);
    addItemButton.textContent = 'Add item to cart';

    firstRow.appendChild(addItemButton);
    verticalContainer.appendChild(secondRow);

    rightDiv.appendChild(verticalContainer);

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


    // checkout button and link

    const checkoutOrderButton = document.createElement('md-elevated-button');
    checkoutOrderButton.id = 'checkoutOrderButton';



    const  handleClickCheckoutButton = async(event) => {
        await checkoutOrder();
        // console.log("Click on Item Button");
    };

    checkoutOrderButton.addEventListener('click', handleClickCheckoutButton);
    checkoutOrderButton.textContent = 'Checkout';



    const linkDiv = document.createElement('div');
    linkDiv.id = 'linkDiv';


    cartDiv.appendChild(checkoutOrderButton);
    cartDiv.appendChild(linkDiv);

    cartDiv.appendChild(cartItemList);


    container.appendChild(cartDiv);


}

async function getProducts(){
    const outputElement = document.getElementById('output');
    const dataContainer = document.getElementById('data-container');
    outputElement.innerHTML = 'status: fetching data';
    dataContainer.innerHTML = '';
    const bearerToken = await getToken();
    outputElement.innerHTML = 'fetching products...';
    const protectedApiUrl = baseUrl + "/products";
    const method = 'GET';
    const CACHE_KEY = 'apiProductsCache';
    try{
        const data = await getProtectedResource(protectedApiUrl, bearerToken, method, CACHE_KEY);
        const header = "Products list";
        createTable(data, header);
       outputElement.innerHTML = 'status: products list is available.';
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
        // console.log(data);

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
        // console.log(data['id']);

    }
    const cartId = sessionStorage.getItem('CART_ID');
    const url =  baseUrl + "/carts/" + cartId + "/items";
    const productInfo = { productId:productId };
    const dumdum = await postData(url, productInfo);
    if(dumdum)
    {
        await populateCartList(cartItemList, cartField)

    }
    numCartItems++;

}

async function removeItemFromCart(productId){
    console.log("removing " + productId);
    const cartId = sessionStorage.getItem('CART_ID');
    const url =  baseUrl + "/carts/" + cartId + "/items/" + productId;
    await deleteData(url);
    const cartItemList = document.getElementById('cartItemList');
    const cartField = document.getElementById('cartField');

    await populateCartList(cartItemList, cartField);

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
            // console.log(data);
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
            // console.log(data);
            return data;
        }

    }catch(error){
        console.error('Post data error:',error);
    }
}

async function deleteData(url){
    try{
        const dataResponse = await fetch(url,{
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        });
    }catch(error){
        console.error('Delete data error:',error);
    }
}

async function populateCartList(cartItemList, cartField){

    const cartId = sessionStorage.getItem('CART_ID');
    const cartUrl =  baseUrl + "/carts/" + cartId;
    cartField.value = '';
    cartItemList.innerHTML = '';

    const data = await getData(cartUrl);

    // console.log("***********  cart data returned for " + data.id);
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
            // console.log("removing product id ....  " + productId);


        }
        else
        {
            // console.log("failed to get the item from cart");
        }
        // console.log("Click on the cart list");
    };

    const items = data.items;

    items.forEach(item => {
        const record = item.product.name + ":" + item.quantity;
        // // console.log(record);
        const cartListItem = document.createElement('md-list-item');
        cartListItem.id = item.product.id;
        cartListItem.textContent = record;
        cartListItem.addEventListener('click', handleClickCartList);
        cartItemList.appendChild(cartListItem);
    })

}

async function checkoutOrder(){
    console.log("Checkout order");
    const cartId = sessionStorage.getItem('CART_ID');
    const url =  baseUrl + "/checkout";
    const cartInfo = { cartId:cartId };
    const bearerToken = await getToken();
    const response = await postProtectedResource(url,bearerToken,cartInfo);
    console.log("got checkout URL as : " + response.checkoutUrl);
    window.open(response.checkoutUrl);



}