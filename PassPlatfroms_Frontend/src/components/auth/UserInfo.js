import Cookies from 'js-cookie';
import * as CryptoJS from 'crypto-js';
import {getActiveUserToken} from "./authUtils";

const UserProfile = (function UserProfile()
{
    const key = "Zift";

    let userID = "";
    let userName = "";
    let userRole = "";

    const getUserID = async function getUserID()
    {

        if (userID === "")
        {
            await getInfo().then(() =>
            {
                return userID
            });
        }
        else
        {
            return userID;
        }
    };

    const setUserID = async function setUserID(r_userID)
    {
        userID = await r_userID;
        await setInfo();
    };

    const getUserName = async function getUserName()
    {

        if (userName === "")
        {
            await getInfo().then(() =>
            {
                return userName
            });
        }
        else
        {
            return userName;
        }
    };

    const setUserName = async function setUserName(r_userName)
    {
        userName = await r_userName;
        await setInfo();
    };

    const getUserRole = async function getUserRole()
    {

        if (userRole === "")
        {
            await getInfo().then(() =>
            {
                return userRole
            });
        }
        else
        {
            return userRole;
        }
    };

    const setUserRole = async function setUserName(r_userRole)
    {
        userRole = await r_userRole;
        await setInfo();
    };

    const getAuthToken = async function getAuthToken()
    {
        // call the microsoft function which gets the token
        return await getActiveUserToken().then((data) => {console.log(data); return "Bearer " + data});

        // TODO - on error, logout and ask user to re login

        // if (authToken === "")
        // {
        //     console.log("auth token taken from cookie");
        //     await getInfo().then(() =>
        //     {
        //         return authToken
        //     });
        // }
        // else
        // {
        //     console.log("auth token taken from variable");
        //     console.log("auth token expires at: " + expTime);
        //     console.log("auth token expires at: " + moment(expTime).format("H:mm a D/M/YYYY"));
        //     return authToken;
        // }
    };

    // const setAuthToken = async function setExpTime(r_authToken)
    // {
    //     authToken = await r_authToken;
    //     await setInfo();
    // };

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
                userID = parsedData.userID;
                userName = parsedData.userName;
                userRole = parsedData.userRole;
            }
            else
            {
                // logout
                console.log("no token information")
            }
        }
        else
        {
            // logout
            console.log("no cookie information");
        }


    }

    async function setInfo()
    {
        if (userID !== "" && userName !== "" && userRole !== "")
        {
            const data = {"userID": userID, "userName": userName, "userRole": userRole};
            await Cookies.set("ID_INF", CryptoJS.AES.encrypt(JSON.stringify(data), key).toString());
        }
    }

    return {
        getUserID, setUserID,
        getUserName, setUserName,
        getUserRole, setUserRole,
        getAuthToken
    }

})();

export default UserProfile;
