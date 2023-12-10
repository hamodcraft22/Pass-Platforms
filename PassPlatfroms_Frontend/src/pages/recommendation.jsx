import {Helmet} from 'react-helmet-async';

import {RecommendationView} from '../sections/recommendation/view';

// ----------------------------------------------------------------------

export default function RecommendationPage() {
    return (
        <>
            <Helmet>
                <title> Recommendations </title>
            </Helmet>

            <RecommendationView/>
        </>
    );
}
