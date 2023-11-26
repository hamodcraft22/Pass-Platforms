import Cookies from 'js-cookie';
import * as CryptoJS from 'crypto-js';

const UserProfile = (function UserProfile() {
    let full_name = "";

    const getName = function getName() {

        const encData = Cookies.get("ID_INF");

        if (encData != null)
        {
            const decData = CryptoJS.AES.decrypt(encData, 'zift');
            const parsedData = JSON.parse(decData.toString(CryptoJS.enc.Utf8));

            full_name = parsedData.userID
        }

        return full_name;    // Or pull this from cookie/localStorage
    };

    const setName = function setName(name) {
        // Also set this in cookie/localStorage

        const data = {"userID":name,"role":"student","apikey":"sjkldfsdkl"};

        // encrypting api key for user type
        Cookies.set("ID_INF",CryptoJS.AES.encrypt(JSON.stringify(data), "zift").toString());

        full_name = name;
    };

    return {
        getName,
        setName
    }

})();

export default UserProfile;