import Cookies from 'js-cookie';
import * as CryptoJS from 'crypto-js';

const UserProfile = (function UserProfile() {
    const key = "Zift";


    let userID = "";
    let userName = "";
    let userRole = "";

    let expTime = "";
    let authToken = "";

    const getUserID = async function getUserID() {

        if (userID === "")
        {
            await getInfo().then(() => {return userID});
        }
        else
        {
            return userID;
        }
    };

    const setUserID = async function setUserID(r_userID) {
        userID = await r_userID;
        await setInfo();
    };

    const getUserName = async function getUserName() {

        if (userName === "")
        {
            getInfo().then(() => {return userName});
        }
        else
        {
            return userName;
        }
    };

    const setUserName = async function setUserName(r_userName) {
        userName = await r_userName;
        await setInfo();
    };

    const getUserRole = async function getUserRole() {

        if (userRole === "")
        {
            await getInfo().then(() => {return userRole});
        }
        else
        {
            return userRole;
        }
    };

    const setUserRole = async function setUserName(r_userRole) {
        userRole = await r_userRole;
        await setInfo();
    };

    const getExpTime = async function getExpTime() {

        if (expTime === "")
        {
            await getInfo().then(() => {return expTime});
        }
        else
        {
            return expTime;
        }
    };

    const setExpTime = async function setExpTime(r_ExpTime) {
        expTime = await r_ExpTime;
        await setInfo();
    };

    const getAuthToken = async function getAuthToken() {

        if (authToken === "")
        {
            await getInfo().then(() => {return authToken});
        }
        else
        {
            return authToken;
        }
    };

    const setAuthToken = async function setExpTime(r_authToken) {
        authToken = await r_authToken;
        await setInfo();
    };

    async function getInfo()
    {
        // get cookie
        const encData = Cookies.get("ID_INF");

        // check if it is there / not expired
        if (encData != null)
        {
            const decData = await CryptoJS.AES.decrypt(encData, key);
            const parsedData = await JSON.parse(decData.toString(CryptoJS.enc.Utf8));

            if (parsedData !== null)
            {
                expTime = await parsedData.expires;

                // check date, if expired
                if (Date.now() >= parsedData.expTime)
                {
                    // logout
                }
                else
                {
                    // get the info and parse it
                    userID = parsedData.userID;
                    userName = parsedData.userName;
                    userRole = parsedData.userRole;
                    authToken = parsedData.authToken;
                }
            }
            else
            {
                // logout
            }
        }
        else
        {
            // logout
        }


    }

    async function setInfo()
    {
        if (userID !== "" && userName !== "" && userRole !== "" && expTime !== "" && authToken !== "")
        {
            const data = {"userID":userID, "userName":userName, "userRole":userRole, "authToken":authToken};
            await Cookies.set("ID_INF",CryptoJS.AES.encrypt(JSON.stringify(data), key).toString(), { expires: expTime});
        }
    }

    return {
        getUserID, setUserID,
        getUserName, setUserName,
        getUserRole, setUserRole,
        getExpTime, setExpTime,
        getAuthToken, setAuthToken
    }

})();

export default UserProfile;
