import {Helmet} from 'react-helmet-async';

import {TranscriptView} from '../sections/transcript/view';

// ----------------------------------------------------------------------

export default function TranscriptPage() {
    return (
        <>
            <Helmet>
                <title> Transcript </title>
            </Helmet>

            <TranscriptView/>
        </>
    );
}
