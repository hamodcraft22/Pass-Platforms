import {Helmet} from 'react-helmet-async';

import ProfileView from '../sections/profile/view/profile-view';

// ----------------------------------------------------------------------

export default function ProfilePage()
{
    return (
        <>
            <Helmet>
                <title> Profile </title>
            </Helmet>

            <ProfileView/>
        </>
    );
}
