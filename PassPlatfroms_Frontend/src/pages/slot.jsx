import {Helmet} from 'react-helmet-async';

import {SlotView} from '../sections/slot/view';

// ----------------------------------------------------------------------

export default function SlotPage()
{
    return (
        <>
            <Helmet>
                <title> Slot </title>
            </Helmet>

            <SlotView/>
        </>
    );
}
