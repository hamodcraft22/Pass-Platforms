import {Helmet} from 'react-helmet-async';

import GlobalNotice from '../sections/globalNotice/view/global-notice';

// ----------------------------------------------------------------------

export default function NoticePage()
{
    return (
        <>
            <Helmet>
                <title> Global Notice </title>
            </Helmet>

            <GlobalNotice/>
        </>
    );
}
