import {Helmet} from 'react-helmet-async';

import {RecommendationsView} from '../sections/recommendations/view';

// ----------------------------------------------------------------------

export default function RecommendationsPage() {
    return (
        <>
            <Helmet>
                <title> Recommendations </title>
            </Helmet>

            <RecommendationsView/>
        </>
    );
}
