import { useEffect, useState } from "react";
import BookModel from "../../models/BookModel";
import { SpinnerLoading } from "../Utils/SpinnerLoading";
import { StarsReview } from "../Utils/StarsReview";
import { CheckoutAndReviewBox } from "./CheckouAndReviewBox";
import { LatesReview } from "./LatesReivew";
import { useAuth0 } from "@auth0/auth0-react";
import ReviewRequestModel from "../../models/ReviewRequestModel";

export const BookCheckoutPage = () => {

    const { isAuthenticated, getAccessTokenSilently } = useAuth0();

    const [book, setBook] = useState<BookModel>();
    const [isLoadingBook, setIsLoadingBook] = useState(true);
    const [httpError, setHttpError] = useState(null);

    //Review state
    const [reviews, setReview] = useState<ReviewModel[]>([]);
    const [totalStart, setTotalStar] = useState(0);
    const [isLoadingReview, setIsLoadingReview] = useState(true);

    const [isReviewLeft, setIsReviewLeft] = useState(false);
    const [isLoadingUserReview, setIsLoadingUserReview] = useState(true);


    //Loans Count State
    const [currentLoansCount, setCurrentLoansCount] = useState(0);
    const [isLoadingCurrentLoansCount, setIsLoadingCurrentLoansCount] = useState(true);

    // Is Book Check Out?
    const [isCheckedOut, setIsCheckedOut] = useState(false);
    const [isLoadingBookCheckedOut, setIsLoadingBookCheckedOut] = useState(true);

    // Payment
    const [displayError, setDisplayError] = useState(false);



    const bookId = (window.location.pathname).split('/')[2];

    useEffect(() => {
        const fetchBooks = async () => {
            const baseUrl: string = `${process.env.REACT_APP_API}/books/${bookId}`;

            const url: string = `${baseUrl}?page=0&size=6`;

            const response = await fetch(url);

            if (!response.ok) {
                throw new Error("Something went wrong!");
            }

            const responseJson = await response.json();

            const loadedBooks: BookModel = {
                id: responseJson.id,
                title: responseJson.title,
                author: responseJson.author,
                description: responseJson.description,
                copies: responseJson.copies,
                copiesAvailable: responseJson.copiesAvailable,
                category: responseJson.category,
                img: responseJson.img
            };


            setBook(loadedBooks);
            setIsLoadingBook(false);
        };
        fetchBooks().catch((error: any) => {
            setIsLoadingBook(false);
            setHttpError(error.message);

        });
    }, [isCheckedOut]);

    useEffect(() => {
        const fectchBookReview = async () => {
            const reviewUrl: string = `${process.env.REACT_APP_API}/reviews/search/findByBookId?bookId=${bookId}`;

            const responeseReview = await fetch(reviewUrl);

            if (!responeseReview.ok) {
                throw new Error("Something went wrong!");
            }

            const responeseJsonReviews = await responeseReview.json();

            const responseseData = responeseJsonReviews._embedded.reviews;

            const loadedReviews: ReviewModel[] = [];

            let weightedStartRviews: number = 0;

            for (const key in responseseData) {
                loadedReviews.push({
                    id: responseseData[key].id,
                    userEmail: responseseData[key].userEmail,
                    date: responseseData[key].date,
                    rating: responseseData[key].rating,
                    book_id: responseseData[key].book_id,
                    reviewDescription: responseseData[key].reviewDescription
                });
                weightedStartRviews = weightedStartRviews + responseseData[key].rating;
            }

            if (loadedReviews) {
                const round = (Math.round((weightedStartRviews / loadedReviews.length) * 2) / 2).toFixed(1);
                setTotalStar(Number(round));
            }

            setReview(loadedReviews);
            setIsLoadingReview(false);

        };

        fectchBookReview().catch((error: any) => {
            setIsLoadingReview(false);
            setHttpError(error.message);
        });
    }, []);

    useEffect(() => {
        const fetchUserReviewBook = async () => {
            if (isAuthenticated) {
                // const url = `${process.env.REACT_APP_API}/reviews/secure/user/book/?bookId=${bookId}`;
                const accessToken = await getAccessTokenSilently();
                const url = `${process.env.REACT_APP_API}/reviews/secure/user/book?bookId=${bookId}`;

                const requestOptions = {
                    method: 'GET',
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                        'Content-Type': 'application/json'
                    }
                };
                const userReview = await fetch(url, requestOptions);
                if (!userReview.ok) {
                    throw new Error('Something went wrong');
                }
                const userReviewResponseJson = await userReview.json();
                setIsReviewLeft(userReviewResponseJson);
            }
            setIsLoadingUserReview(false);

        }
        fetchUserReviewBook().catch((error: any) => {
            setIsLoadingUserReview(false);
            setHttpError(error.message)
        })
    }, [isReviewLeft])

    useEffect(() => {
        const fetchUserCurrentLoansCount = async () => {
            if (isAuthenticated) {
                const accessToken = await getAccessTokenSilently();
                const url = `${process.env.REACT_APP_API}/books/secure/currentloans/count`;
                const requestOptions = {
                    method: 'GET',
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                        'Content-Type': 'application/json'
                    }
                };
                const currentLoansCountResponse = await fetch(url, requestOptions);
                if (!currentLoansCountResponse.ok) {
                    throw new Error('Something went wrong!');
                }
                const currentLoansCountResponseJson = await currentLoansCountResponse.json();
                setCurrentLoansCount(currentLoansCountResponseJson);
            }
            setIsLoadingCurrentLoansCount(false);

        }
        fetchUserCurrentLoansCount().catch((error: any) => {
            setIsLoadingCurrentLoansCount(false);
            setHttpError(error.message);
        })
    }, [isAuthenticated, isCheckedOut])

    useEffect(() => {
        const fetchUserCheckedOutBook = async () => {
            if (isAuthenticated) {
                const accessToken = await getAccessTokenSilently();
                const url = `${process.env.REACT_APP_API}/books/secure/ischeckedout/byuser?bookId=${bookId}`;
                const requestOptions = {
                    method: 'GET',
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                        'Content-Type': 'application/json'
                    }
                };
                const bookCheckedOut = await fetch(url, requestOptions);

                if (!bookCheckedOut.ok) {
                    throw new Error('Something went wrong!');
                }

                const bookCheckedOutResponseJson = await bookCheckedOut.json();
                setIsCheckedOut(bookCheckedOutResponseJson);
            }
            setIsLoadingBookCheckedOut(false);
        }
        fetchUserCheckedOutBook().catch((error: any) => {
            setIsLoadingCurrentLoansCount(false);
            setHttpError(error.message);
        })
    }, [isAuthenticated])

    if (isLoadingBook || isLoadingReview || isLoadingCurrentLoansCount || isLoadingBookCheckedOut || isLoadingUserReview) {
        return (
            <SpinnerLoading />
        );
    }

    if (httpError) {
        return (
            <div className='container mt-5'>
                <p>{httpError}</p>
            </div>
        );
    }

    async function checkoutBook() {
        const accessToken = await getAccessTokenSilently();
        const url = `${process.env.REACT_APP_API}/books/secure/checkout?bookId=${book?.id}`;
        const requestOptions = {
            method: 'PUT',
            headers: {
                Authorization: `Bearer ${accessToken}`,
                'Content-Type': 'application/json'
            }
        };
        const checkoutResponse = await fetch(url, requestOptions);
        if (!checkoutResponse.ok) {
            setDisplayError(true);
            throw new Error('Something went wrong!');
        }
        setDisplayError(false);
        setIsCheckedOut(true);
    }

    async function submitReview(starInput: number, reviewDescription: string) {
        let bookId: number = 0;

        if (book?.id) {
            bookId = book.id;
        }

        const reviewRequestModel = new ReviewRequestModel(starInput, bookId, reviewDescription);

        const url = `${process.env.REACT_APP_API}/reviews/secure`;
        const accessToken = await getAccessTokenSilently();
        const requestOptions = {
            method: 'POST',
            headers: {
                Authorization: `Bearer ${accessToken}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(reviewRequestModel)
        };
        const returnResponse = await fetch(url, requestOptions);
        if (!returnResponse.ok) {
            throw new Error('Something went wrong!');
        }
        setIsReviewLeft(true);
    }




    return (
        <div>
            <div className="container d-none d-lg-block" >
                {displayError &&
                    <div className='alert alert-danger mt-3' role='alert'>
                        Please pay outstanding fees and/or return late book(s).
                    </div>
                }
                <div className="row mt-5">
                    <div className="col-sm-2 col-md-2">
                        {book?.img ?
                            <img src={book?.img} width="226" height="349" alt="Book" />
                            :
                            <img src={require("../../Images/BooksImages/book-luv2code-1000.png")} width={226} height={349} alt="Book" />

                        }

                    </div>

                    <div className="col-4 col-md-4 container">
                        <div className="ml-2">
                            <h2>
                                {book?.title}
                            </h2>
                            <h5 className="text-primary">
                                {book?.author}
                            </h5>
                            <p className="lead">
                                {book?.description}
                            </p>
                            <StarsReview Rating={totalStart} size={32} />
                            {/* <CheckoutAndReviewBox book={book} mobile={false} /> */}

                        </div>
                    </div>
                    <CheckoutAndReviewBox book={book} mobile={false} currentLoansCount={currentLoansCount}
                        isAuthenticated={isAuthenticated} isCheckedout={isCheckedOut}
                        checkoutBook={checkoutBook} isReviewLeft={isReviewLeft} submitReivew={submitReview} />
                    <hr />
                    <LatesReview review={reviews} bookId={book?.id} mobile={false} />
                </div>

                <div className="container d-lg-none mt-5">
                    {displayError &&
                        <div className='alert alert-danger mt-3' role='alert'>
                            Please pay outstanding fees and/or return late book(s).
                        </div>
                    }
                    <div className="d-flex justify-container-center align-items-center">
                        {book?.img ?
                            <img src={book?.img} width="226" height="349" alt="Book" />
                            :
                            <img src={require("../../Images/BooksImages/book-luv2code-1000.png")} width={226} height={349} alt="Book" />

                        }

                    </div>
                    <div className="mt-4">
                        <div className="ml-2">
                            <h2>
                                {book?.title}
                            </h2>
                            <h5 className="text-primary"> {book?.author} </h5>
                            <p className="lead"> {book?.description}</p>
                            <StarsReview Rating={totalStart} size={32} />
                        </div>
                    </div>
                    <CheckoutAndReviewBox book={book} mobile={true} currentLoansCount={currentLoansCount}
                        isAuthenticated={isAuthenticated} isCheckedout={isCheckedOut}
                        checkoutBook={checkoutBook} isReviewLeft={isReviewLeft} submitReivew={submitReview} />
                    <hr />
                    <LatesReview review={reviews} bookId={book?.id} mobile={true} />
                </div>
            </div>
        </div>
    );
}