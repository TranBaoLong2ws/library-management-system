import { Carousel } from "./Component/Carousel";
import { ExploreTopBooks } from "./Component/ExploreTopBooks";
import { Heros } from "./Component/Heros";
import { LibraryService } from "./Component/LibraryService";

export const HomePage = () => {
    return (
        <>
            <ExploreTopBooks />
            <Carousel />
            <Heros />
            <LibraryService />
        </>
    );
}