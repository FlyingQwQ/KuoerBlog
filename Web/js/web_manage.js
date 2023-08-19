loadChain();

function loadChain() {
    let items = $('.friendChain .items');

    $.ajax({
        url: api + 'friendchain/findFriendChainAll',
        type: 'get',
        success: function(target) {
            items.html('');
            target.data.forEach(element => {
                let item = $(`
                    <div class="item" fid="${element.id}">
                        <label for="inputwebIcon-${element.id}">
                            <img class="webIcon" src="${element.icon}">
                        </label>
                        <input type="file" id="inputwebIcon-${element.id}" style="display: none;" accept=".png,.jpg,.jpeg,image/png,image/jpg,image/jpeg" disabled>
                        <div class="info">
                            <a class="title" href="#">${element.title}</a>
                            <p class="subTitle">${element.subtitle}</p>
                            
                            <input type="text" class="inputTitle" style="display: none; outline: none;">
                            <input type="text" class="inputSubTitle" style="display: none; outline: none;">
                            <input type="text" class="inputUrl" style="margin-top: 5px; display: none; outline: none;" value="${element.url}">
                        </div>
                        <div class="button-group">
                            <button class="modify">修改</button>
                            <button class="remove">删除</button>
                            <button class="cancel" style="display: block;">取消</button>
                        </div>
                    </div>
                `);

                items.append(item);
            });

            let defaultImg = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAF0AAABSCAYAAAA2J9gWAAAgAElEQVR4XtWdeXDdV3XHr2xL1mpbthZLXuR93wImsUNIQhLSwJQkJdNCaYHCdIZhaGdIF5h26DBtgSlMOzCdzpQ/uswAM4WhYQkdIGlCCSRxFpPEduLdsuVFkmVZkq1dsq3ez3n6/nze7/2epMSBTs8/773f/vvec7/n3HPOva/kyc6nJ+5Y9LaAjF8dDKWzq0J7e1soLSsNC2rnh6vjY7ZvZPRKKJ87xz4l4+PjyXe+lJaWBraN9vSF8aHh0DcwEOob6sPchQvyjkvOHxu3+4y7z7LxK2GsdE7cNhaqSmbZofyeSjhWcq3vcnht78uhsqYmVC9eGBpbVmSeOjY6UvSSYyOjoXr+/MAxteWV4eDhI2HVyhXhanzW0rIye7b+rguhu/18GBoZtussX7UibLn51sxrPnXxxfC9M0+G31p2dwDrkivjoxP+SIC/cL47LFw4L8wuLbNdHvi8Y1Oga1/fxYth9FJ/GBoeDM1NS0J1Y701BuIBBnBJyeBwmKiqsP0APzI6ai8J8Fmg8+ICgE+E8/p7+8LFfUfD5fJZYdbcsrBk45pQNrd8ykYr1gCc17i4Lux/fm+oXbzYrlNZXWXX4nnPnDkbLnX3GPCV5RVhy03bQl3L6mmBLwC97dSxML92gWk1oAN4lpbryuXXYqtfLUluNDQwGNC2wbFR0wQeZM6CeQno6ScC5IVhPPSXVSYvA8iD/ZeTl+SYtKhHpLdfPXIi9I+Mhc7Ll2zXsh2bioIO2ACpxkuDX1UzL9Q31oUjv9xnyrBwabMdW1lVaficaTsbOk6dCQOxoZGmpUvClt07jS2yRBqfB/rISG/oHxg2WknL4MCQ3UjiaYZtgD8yqywMnL9g1DLccSH0h6th9aYNpsFZYHtNZ3/N7InQM5yjr94LF8L8ikqjpizQC683FmZHYC6fOB1qystCe9S+i5cuWbeviRSXJQJd2ovCCHj1DhTwQmdnOHfoeFi5Y0sCOtfjXS9EmpG2s+1t99wZ6uuaE6pO3/dTr/zddXqBVk4dbw0r1qyy49KUIj6vqq4MagCAhzbE5fYgZztMywcv9tp10qCLXjJRiBsFMKDDqwIkDbwaTNeju0NJ506cDM2xq5dWzAovnWoPi+I1lm3fYjycJWgu1+IdLkWNzQL9Suy5L+x5Pmx7y46E1zkH2zU8cdW0/drwqNEM2n7TbXcUe72AYiea3ncptli8acuKtdZKEsCHZgAawPVbtKPjRDOAjpZLGjatzdR0XlIivmfb0OCQbe45ey4sWLAgMcIC3RtejlOD00u6L1wyTV9YWx4WLF8ajp/tCl0nz2RSjChFxhoK5N7QmkTUwz0PPPmL0LhhdSgrn1tASb2xJwx09pgNQTbfdsuU2m6gA3L7mXYzGjKeaW0Xx+uBaAREms53NKLz/Pkw6+Jle/Ge3pFQDPSa6ooCW+GNracXu4/jdU9LvpddPX7U7mmgr19rz3f8WFuYO78mwM/FtB1aKp87NwxOXEs0nXNFMfS21hdfDlWLau1aiK7HuX19fXmejLRd3mDSipNf8kCXlnvt9id44NOaznG9J08nWg6vYtCyQAeotH3wNAXAXtM9JXmN95rOdoyobYvUYqBHm9B9usN4d9mypYkr6t9JgENNCHThpaJktjUIngpSXb8wjWHAxbzY2WUGFS8G2XXfnaG8vLbgWDYY6PjlCJa6mHvIfgElI8pvDC+ilwZoAOfFszRdAHp6sfNTtuHs4WP2gmjUVIInwblwetfBY3bvkiWLQ119zhkYuDxigMnz0LWk9YCOa8pnGnAdC/A0XEekvJaN6/IeB7qZMzxmYxL2Szhu3ZaNmZ5MyfBIzwR+eXNzS4DXpc3Qhx/8pEESULoJRgU+Reje82Mrd/deDFeiJ1S9tCl5GHx4gJTxSt+D33gR59tOhaoFtaG2vt6OZRsin1wXFMdfi/t1/1Xb10Qtv+4xwfVQwOLGxoRCRB0CG2CnAr2941zojrYKb6hkkrvTynAh2g9EA6Zb3/0us4NpF7Kku/vsBAZEoCeaUGTgIw5Nf2JAuzo6E88BQ9bRlfOV06D7QUaWFtMbzh47GiorqqKGLrFDMHBZWi/QR7u6E2oz0BEBPzwcDrWes9ExWj0QXUmNOAFbkgYd2vCafC72viUb1oaymmw/XBSj623e9TZzTNJScrrt6ARdVK5gGvS0i+cNl46lazMSxGuRlpc0VIWB8RIzrEsjv9KwANR1rt1eRNpeDPTjB16L11oQGqNmSfOXrl2XhA00ItX5GhRVNNWHpobJccYk6AzeeMbzfb0Gds/ZdqMuAZ5FLwKcz8qJEqOPIwcOhFWb1lsPRMb6B/MagN+95zpt30i0DXXxWbbdsrOA20tOHHt1onlZczLy5ATf5afzq607xa49eChnxOSuCQy0HU3XdTzoHJN2AdkGBeH71lRVmQHEq2g7ciy0xMbzsQ/1AhoAIOf2D4d5q5cnfJ5uUHkyhCgQXFIE0OWfAzKCYug7vydGx8Krv3wpNK9aFRYtbrDfohl955NR+LX4CegY1TWxkZau25T3KKbpGFBxuNfkYm6arqD98Gnnnn2hcXljznOI1KKu3dHWaaArtnK+9ZSdDr/6mIrAZ3jfOzJkI8D5dQsNdH7TCAIdd3Iwau3KzZutMbm/BkUVq1pC9bxUrCXSC8/DswyV5EJNnE9cCGEEfaUi52N7oD1SAn1xPKeuuTHZ5ZWSRoBirvTlGhVpWLmsIBBW0tFxcoJhv0BHa7NGgX4E6B8GLYMjh185FmmkyQwo1OJBr4k0IYFuBLq9ZCqC6EGvrK02YDTq86ADAprO/dHSntbToaW2Ksxes87CCSaTYOveeDLcv6osuoDxc1ls+NLKCvNMFlRX22FqFP+OfBfoDQvrDchiQiOIYjimal512P7OW/MoxgypLqAR2YJFi2xTVkTQa7/cLnXthE9TngN+Ln6w/N3xq2Nh1YpVRhuMCPlEoA4P+qyKuRZSxfPoiTZj04b1CdU0rViWjAzpPVDLyqamMLqyOZQPRgPunsGDj7YDdOvJU2YzoBg8k/r5C4sCLtBP7T8YZkVvBA/GS3rsIIrhGEapaYopGejvnJDfTRhAL1+0KSd3CHBAaz3VGpquzc75x9W5bhoW5hpu4HxPcimAR6vG+gcsJoMAeNoNZGDEg1cQml290kAZGRk30DHYbadOW8hWhpBrNs2ZyKO1rOdH03kGGpGw88RQHLw1LQ4XLvUUBb176URYdCKOA+KznD98wrg6C3Q/SibuNHh5wACH32sjLsRjNEJNQJd/PBPQPeCAwEMvnxuTHnB5lO6BMQPX/Ov4cqWzy8xdQzRkJuQr0D1ANCJeBj4voDP0pgHQekCngYd6B0x7oB0GJiePHLdLbGppDMPluWE6Aq0R4TS6iaPTMDxkgyUNoohEAjquLp9IMXphH8800H0xNKxtMXcWSWs5jVPS3p2ElgGeZ7/5jt0JxRjoGlXiNSjIk6UpAlshUcUdAJaH4LMrxjqQ6rpFAU6WkATQyA7O27ZrZ0IrflQIBcG7AI1gTAmd0gD0jldf3p/0APZPxGudP33ekhaL5823l5WGGYj9/ZZFYh8hCYzsiRgagKPRSMCm57SsWF4AOq6ib4RioKcjnp7X9Swb3rHLxkKIcbpcRIGOIU2HUj3gnEjjANbxg0dsBEYIFSrQ8DtBO2rYiRgTITaOoOlw447bb02G37hsEjQd0IkOKmon8OktR18+YMaJe3Fe56GjoSbMvu4qTt4HrZZ2n780ZMYWjwgBYMYUXaPD5okIfGuk6N0I7CzQOyPV4TZiD7IVM5dwkTHlHcCHsMDm7TvzQQfkrFFfVmQObwUQRRW8RFNLrnvaizohakmcHhqQp0CMgti0xI8EyUlCL/CnQBcvolGno9FUA9MrRBX166OmynhOPkM6YortIhKqNFvD3FyoQODj1Uiz04BzHJoO6Gm3Ue/hqUbGVKD7rFKBpqeH2mnQ0RgGF1h9GcU8Dc8Ava/3kiU3JIdiopeH2Lh5jcXAAV2xDz7xm+nyCIBDESu2bTKNxECRcMb78aNgRRU5R2nGTFWc3Hho7/5o2HKJDWwEGg8fa6DjQde2gUh5J0+dTEBPe3cCHfeT50fDy+P7SHkUeTQ/XQ/HaJEAk5c06OQ/leXHq8BIMoCxAUkKcH8dKIYXQfCRCYPedfvNZnSl6fJGAB3jBsAS8TW/8QagGhpdrmLJhhwnp6VYA3QcbrV7jM4qsawPtiMvUT7ZAFxP4NLoF851mL2ikdKJFRoRPLgOLq5yp4xMaQACYAvm1xdyutf0NOAynNyMlwZ0DCijy+rGwjizB4AkriiG0SMPv+td77RDGHFKAB7a4gWHo5aj6WgKVEDOlYYAdLSJxq8bvxqgltmNTXlZrbTGp8Hv6blsQTVc0SzQ/bPLUfCg00h5yjkJuLbhFpOjRQS68qdTanoWn1PvodIKBhjEIVavzy87yNKuYqDjN6eje9CX/HRpO8YTg4l34kFP/PPJcYHncQ+K8gTJtmho90eDPhXoKJcoCA0GSGm6QJd2++O4R//gYIGmr71pq+WgE043NyfGNBqWxEw2N3OJ3CwXkeE5oDNiNI+lCLWoAXBL4fXFYY7F2fedaA2bd95kxhXQFfMQBeF7e9cP0Ek4H4uNLtDnRFtBvB4jPnvedW9CmS9ePv1doPM80CmxEoTgWrrwSUCyX6CL06cDnQZCYRT44hr1MXxAYiPPkPIQAj3HZTlDI9DlztHNGLSQC7XYtdMyn7jWCwK8jCkgy1BCGUQFFXDy8ess0DWQQeNHa3KeB/QC8FQgpN1c4t68j0pKfDKBrDx2RqCXl193WwHYAy7Q0d721tZA/IWGTx+j4wy7SDckPTy9KJuUxF6IuzD8Jn4tw5FVBwK9YCTmXpuw7u5B5wYCmAYkksdQu6ym2romHI0Bqos1MqTyEOI1CK6aEgb89qCjLXg78LjA9UYPT0qJZTW0/8TF5VmgEkA72dEV9h86bIe0d+UGYfb99JnQ1t4eXvrFT+0daQgaQALol2MCn/gLsfLphAoBabr31fM0ncCRB53BkkSDIRk5ttPdvRHLe4ieGBOPsXTRCXEZRo+I8qcCnbCqTxowBsDYqntyHC9J8kC+sjVMBngCbjpAptr/8gtPJz2AQZB60ExBV08R6LiNitlQ71gUdB6KwZLAAHRoRplvLD5auybGIeBzld7hij2/Z0/45299xzTHy86dO8MDd90etq1YY54I1r06vhSu1eXeXGAMIJGfPPVUonk3AuAbOffpx39ovQKB6xHoQj2lurQ81C+JOYLK670g6z74/6pz5FNFpgWgKxvjQee7cooDF3osFkIQh/BqfYypkMx+7ic/C5/7ylfDnueeDy3NzQXPQLf9/yI3ArroiEZC0xENjlBS0/T04AhwCbXiyWBkxIGcDA/CgWkN/lUCSgOmr5/exu/m5cvetJ7x+Lf/w5RKnC6DKU0HC4zpnAU1dgySZXyLajqgK0WH8bMLRov/L//4tfC5L335166cAPixD/1+wX31LLt33RKaGxqt8elVSNa2N/rg3P9vP/0nFtTKAh0qlBGlEfwxAl6fgK7BXR69eNAxnNTlkXv8vwIdAL/6hb8xzDTAgO8//Kk/KwrwmwE6YP/r1/7JOJz7YrN8aFqNyAh8zbaNsdjyYnjuhQMFx3iNLwq6SjA0g4IBz8a37ghPfue7yYt6rRFfq8vzGwOJPPLoo29UwZLzBLoPRqElD37kYwnoOlj2Q9Si7TzP3r17C2hpqofjPb737W/aIbiLadA9mJRVAPqzv/ilBd+yhOOLhXdLTr328gQ14Co13v/SKzHW/fbwys+fSV5UF/UA66Wytt0o8vjJctNktO+5/30Fl5WGe4Czts30eZ549LsFmu7Bhk7qrs0Ka+99R7h6uS/89LGnivrrAt376UnshboXRpqktQCezAz5RwL+977/dwue96H777dtv0rQn3/yMbsHAyBiMAy5b7n7N4o+y43wO+/zoYceNNcVapFryOBPLmH10NUwUDnbBnrEXggzQzE//f6PXxfoSZTx0Kt7J0gcSE4cPGzhTgYFn/zzvzCj5TXJu4N0a+TNNmiATsiWoT6aTszipptvS1xRURta7T0p78HIo/HPl6XxgP7Zz37adhE3p4cpPej9cMVelHAmNo6bLC8nfW2O9zM02H/Xg++2PKlVeBGyFcUoP8lUD0oriF98/vNfLuDrLC6/EX73537iD/8gGZQIdM3KyLI1OlfuLI3yep4FOkMUVcQAkpMFdGm5B91qWXbtCM8+/nSe2+iBTzcSduk9H/igHWKJacKu0nZSZfjnRA+RSzEE+ugjjxa4j8X4XdrvNXAmfjwa9/Vv/bvdUxOr+I4nwSBM03Ke+eHjBbQ33bOoJ3pQ/voznw6733KTZfehC0RlGcSIBLrOAUR63/5Tx8Mtu3eHuQ11Zvemi8HQgJIkc0R9+rEjByxdRtBIoBMnV8qqLSZ/v/HI98O2jRvCv33jmwVeAd1c8kaphmv84NH/tNGtEgxcE+PFsxASRV7dszf8zoc+ah5TFu3NpIG5DqB/4H0PWCJE1V2Ukmgai9xFaTrniNPvec+dVgWMQoj75bP7htV3DZLe9dsPWNm0TQq40N1usWUGRfLVCS75wkjcKOSvvvilhMN10RsZsHDufXfkJkZ94k//OJl4wAwKZaYITfh5rZr/9OEPfPQNu6mA/t7fvM8oRdFLpdhIfEMt3nMBfOJFCKUgTPzC+DI4KgZ4OnWHV0gZhoFObPn4/kM29c/XsnCSarHRdm6SBXqW0fIGdyrt+/pX/z48+NHfC+c7uy384GdqUx3GbGXRi6bcaMLZV774lSlHzeqBOAMIPTVRlEgtqgag0rjjSkniV8PZhKMRKAXPRZpOOJvqAWiJoBdOh4DnmHRcXgVXaDt2knr1ZHYdU2CIt5CYpsCe5DEXkMZL0xWL2bp8eXjfJ/8oqze9rm2Aft8H3x/OnWrLSzjoIkdfPWRxdj/VUtmoF57aE5555rlYw9gQiPxpajpayTSYS1VxRsjgiCU5JCRRFg2Nh85wxWpfOA6h2gvPBMDTWq5zoReO6eq5EObVLrRSEIGuhIXneGk6tgIBS8scaZq6Mim8oCqx4Cx8V7SdgncNVDRau+3e974ugLPczc984uPmSik0nJXjzMpvMlFYGs/ocKIrPl/ssVQNZ4n28SnRTDw+W8+dtUaTlmdlhQCddB0i0PlOr6CWh3AFkwYkvvROFQKU1yWz6yB4pqgrNeezLSqCV8aI1qYW5eEvfDHv/fBc0l0ZLUQo0GGQA3+ibRjtqSoI/LoEptlxBJjIZF0iv1UUivaSeyVBMj48WQUcv0vYxj5E86E0KY2yvM7xwbC4tCpcbZiXUEsaeHoQ3gtClJEGEuh+jQCMsLRc92cqpOJaiaarovTnj/0oqY7FmmMklLTVLGi6FIXvAM+DMhmgrnaRaRrCC/KbLiyhmLO6lELOyQlYEbj0gg++imBK0ONF/XoENWNDpumAzn39J/ef3T4Rrjbnam4AHtB7X+sOs1bPDmePXC+CYj+gk/+195isBpBBTYNOLQ58z36f/YdiPOgUMWlmB+MNm12nhC3A45LRFSgxw51St1Dtnq9H4cG4cQLs5FRGflM9WzHSb59oNiXUzEHKA54Dp6gi0HWz6IWa855Qaos5cN2qaKiyQPeaz/WkHFDKUFdOSVAatJ7f11bUFVTkWgPEpISnFzSdd2fUrNCBiosUUgC7rJl4eZpOzSFCSg53Ua1NKyLMXNDqFvyuvjSSTHnJ6tIATo7UZmZ48QX7ru4wq3SiAPB4Ha1JQAk0Gi9N93yt24lKpOFsp3GgFGTz9uvzgU52dIRri6KmR4VDeFdhALB4INQyImsqY1VyQ65HCHi0W3NJ0XY4HdA13kHbkby1AeRBUAVACEBaDthk5wljMmMB2uEBAJ0bMwMC8cZKBq0AcA7MKPT0pRuimTT95DUcVbkSprk4gWqyRM8HuAjP7Y3vk3tftNHpdKDjKVFap9oXuYXkIRjnHIuVxWg77OA1PQ90rYCh+e6aaQbxi9OV81OhD+4W3dGD7l80E2wPuH3PBdrS/O2vk6Xp18GeBD4FOvuLAZ/ZGnEj2v/M4YM2xEdUn0NpCL0et4/RKu6iQMfPR8s96Cw7cvboQSshV1Eq19P0R4A3Tcdr0RSU9NJM+Ov45lve+pZkuh4TndAGCjBnd122CV6ahVHspZLtaHkG2B789DV+HcC/tu9gGJhfnkwOYC4qtoKwyFSgy5DyzEvjxF4Km7CRr+3bWzDPlNobfPW8xLQvp/PzKg88+1zi8ikbghGB/4h3r44W2erDpcmpWW257bnpJ1mGM137SMkbFOfXYtG6BJ56cCPx11n2xCZzzUDoAfTCvtOxoLUpF5oebm2zcj3Rpww/FcVai4zj5IurBEOVZl7TfTUZMS2JSsFhD5tHSlyDIBMVXloFSHXonKQYs2EXDYOqaelq67dutaEylV5aEkpTCg0QtxrSVJgQBmBsgIj7dHx6bqemrmzfvj6MVM3PgS6ZIfgcDqiMFzSlhkGOgl+4uGg5goYiTO0ZGB9J6MWXV4MRJePzl+YmR6DtrLXAqkiG22TdPcyRhHapZ9FKEVqNDYd+WctSWziMbkHIl6gaAvBYcs2rpLStbvn1hRc8wHnaObn6Xe4iQ5byQqhoVQ0NgX6/0I+updgLsyme+NGPrTDp4b98+HoP8sbVru+0H1rLaBCihfReDf9xFAy06OYS1ha1eND5rsGef8806OwjvKL1yIhrcc0SMkfszFpADMARZi34UAAjLmW60fbb7r5ryspd/2D+O9q958eP2fmNq3OToCR+7JAGnd+sLfOt7/4gPPzwxwspKw2+v/Ak8Gg58srPnzVPRPFzgS4DyjGKnUjT06Cr2teDbg0XtR3luXQ2p+0Is0dscERFrS8apUiURRQU2wB0FjCgblyDIx5SteOMTO956N1Wrjyl0UuhD3eTCcoC3YOfXt4KKmTBBkbFtz5wrx1adMpLVgNE4AFdgHO+B12AKzmuwQ/Fo2l6UX0616AOlCn/ApxPnp1QNHN0yUVTj2+gs5SUZtSpRl0GgRPgJWZR+AXRFJHDmJyKfjuzDciMpKlkqiJ99nFvxgQrt0b/OFbDTic8D+MJqG5bXNojva5Y3vluhl2yPQKOVwI9QRGyUR50DYqk4ZyL6yzQvab7SQHMgxKnp4HnPTUj3Didbq6l8Dh47fqtCafSC3D4MWaKNHKMKpc094a1UObF1TR233N9pbZiA56sQv30gGgq8LMWcStqN7gQ4LtFGVihFErRahUcgtJAmxr8pe8PnRJDR9B2GozjaQxV9qLpSrb4Wni0HRwlBaBjzLzGFQNdc0c10xifXVW4Hvhi4BWbLTGdpk+1P2tCAoArOMbiZ5q+4r0wrkmRp6KgvAuiunoCfMRVrk0uFudB18w8TQlKLzPin1cLjRaAjpZ7kfVlm+YC8V1rEGJgiS3QJZmZAdUwBNa07LQXktb+qUCUIZpJQ2QCPnki5dtMowQ0YuZeKAbStEOv6QS3SGYAKmUWgM5x2C9Ax2vDryd5ocQFs8LFEnp2/w7S9gR0OB0aEegK9XoHH9CVQVIhJa6k5gnxMsRlWuMQmAJMyvOyVjOdCYgz0eiivSgOmjDqvOSLT/zMkgskHRAV6PtzaQxNq2cuFblZgS5QlRlSHX0x0MlwFVv2Fb+dGS/JanVanCy95hSga5ExDJ6ibTy0jy3wG/ABnQfWdL6NcRGFpVs3JO4Tx70ebZ8J+J6qdPzZA4dtcgK+PKUWmkiLZntRI4jTPegcx3uQluOTY3GRESUx2O41XaBLadXTNVjCizHQNXICXK3NyEFUCWh1OS3ZxA3JktAV0XIF56XtmumsF9PKbUxLT/vivgFej/anQZabijEFbJ4BzsazItOD0fNGM30vNJ0kM0Kmn5U2UBwGgLJd2AKoydMLAyqBTlgXt3oqTSclSo1RXmIaYL2mA7qW5lNYgFAl837SWp7WdB4Y4WURHpYJUswwW7KiZUbu4UwbAtCpHNj/3F47Za1b0Jipk/JUdD1puzie32izQKe0UHwO6Ngo3MVioCtpgX1jTq1G1GleVyAsb1Xp9DKlWiSTh9GyUQyL4XUqwLLmf6Jlcic9aGieKIduunL9moL5n2nNz6KNtMGkPobybgZKBOGU3de9AV3GM00tHCN6SYPOPj0v++T14DZiSJWYlsemzNr6t24vyulck+hjwfrpHiiFfAGcuAGiAiBiLR50aEbejUDPAl/aL24ke75ux45kEU5//yzQrWGioWRZKC2uA9jUrih75a+R1nSv5fQ8QEfwRKAU6EWa7ssqcA6gKQ86Gk5lBKKExlTrp+u5ioKuRY/xajzFEJrUgjlcxANPNM5P4/Mvjx9MponErxdKGjB2d9/9DlvDEEkvGaKBDxQC3wIGho+QsnKg6Vwo12Hb3leP57mJWYaUZ1MZnWaCU/NDrEVlGZop7UHXurpvGuiQPiNVhQcUeeQGWs/Qz+n3Wi7+lhZpurlfv4XvUI6BHBMh//Bf37Pvn/rYR2x1Nw8067Mw4kUzmcTLLOl0qYVKLPwn1/P0wm9Ah8u5FgJwvlgU0JnDiviBlECH470LKuDVaDfdduuU9MJ1i2q6fEpF0DhYKz1rxU62MZkX4+pX39QSTl6jpelkZyTSfNXRUM4h8JlvSrKAbaQEoY8sAWRVcmXtB3SuwXJTEp5PQuhCRaAqm2BVJTyTqUDHZfSzLOQ2QpUsLzKVFAUdPvfrM0rTiT7iayo2jGFVIQ3UIi3nptL0eSPX7KX1yT6/VBTgs5+FzwCRTD1Aa3Vqjk9rsl6qOyZKmPbu+dxTDnUtUJqPiKLlaDsGceWKlTbcV2ERGXxWT0LQaAXF0HSAlsvIdrKDvzEAAARxSURBVD8xVx4MgbsbAp0gGBPACHGi5ZrYy9qJLO1HCFirAWmhMzjPGyjPoWoEAebphgYh1yqZrkorrUlZRhTwAZ3qLbl7+uQZAVXrcSmi6MsoBDrUIUMqepGm+5WLOI71gLVAWjFtz9R0VQcQ/GLYKnfRr5AhuvFazrYsalF8Y8o+F3fuXr8hqRLz4KscTtuosGWdFy++bC7dAFCMFwHPNpb706wJfuOfY6g13KdRqHBjcWO+K7wr0HVdjWgVf5nqXTNB90aUk1W77tfh1URfRqJa+gltRaskCvjrd5pb1Ujav311jFu4+sPpGknUkj6O6i39U4Eoqq13MCn+5HjsBdqOD66yaLZDMz4XCucLdNELx9FwXpnkAUFPxf5kSs+ZCTpG1P8/BTk+UneqrMKFhFrQch95hFoAVtI8J1c82n6lyxpDjcDLymvghUlsU3C/rWlpgaZPB3zW/ix+5zhK5yTU7OiZABPB3+c5VJmrrD+1jSxruGrJUqv18YolKvWgTzdAKigg5eYYUf6URKv/MGz3f8WgpAYDIvx25UvRdAHtwQD0LfffnrdahpVGRwOoFUE5vpiHcqPA6/wsX14NocIpDzqjTq0zw7+EUYJH4amE9/rvE4cCtfpoPmsF0CumGyBlgk5kET7X//5kabnKxUibMSpj9LnhSi4/mBYD/f33FfxDmDI6+/bFUPDkfxNNB7C4+/TouC01Kynmp2ddTyXSfh8eE4WkJ2LUUPWKgK51dRceGw61m+sMdFUAqxj1YFv8D7vohkJFDKqyFqr39yqgF/3fkdJ3rDDtuVz/S8dFNBsNd6yYlnOcQOe7NFxFQgznWYvLu3nTAT+T/VneDOf5QlJ+U8uIh3N86GJ4+4ZN4cD5zjwjqqJZEjTUPqoyOP0Me44cNuAxzNQ0Ul6XJQQRC0BnI6IVSaXl+nsFVj/yK/2g5fjZWbTiuyH0omoBAa+KWwpKWRRzTk9X4o/PBNi0tttzTxb/p0FPFtNPXbjnf9rCS8NxgctoixZtX5dkmOBqrZKqv2ajzh1tz5LB6C5StIRLWey/MDiP+v8C0KEWYsLM5dSi8AoFaJV/P3kJWplKy6XpK+682WZeyBizPSl1nlxkHn6/0nFmWuCzwE4DIc+Gcm37jwzVUKokY7L+5fs/+Ele2TPpRmWSWLmaGR5U+RbTckr0fLHq/o6zFp3U9EX/XJl/GMgBVJzyV5aUOWBANfpk6K/MkZZamo7LvaajRSzlpySxANdf+eAt6a/QqB9kQsFMBa2Gp2WI/UQEu4ar7rKJBBW5Pz584pFYhjHpbTECpgKXGA9GkSJZYkxad6yYlpe01IWJtuvGlUbARlXHVYy0oDH3EuAPL3golKT/BJZqJLoJ0++8aBFMtgl0YtjTabk0nZdatWtr3rQV/7c9HGd1j1ET4fnXA7qeM++vIVTEmi6t4+C4rXXfcXP/oMW+hfGPayeXHKTEjnJwrYYHlytGj+FMS1rT2c+2Fw4ctf/w4E9enu07mPcnsP8LUNPqwaQvu2gAAAAASUVORK5CYII=';
            let addItem = $(`
                <hr>
                <div class="addItem">
                    <label for="addInputwebIcon">
                        <img class="webIcon" alt="icon" style="border: 1px solid" src="` + defaultImg + `">
                    </label>
                    <input type="file" id="addInputwebIcon" style="display: none;" accept=".png,.jpg,.jpeg,image/png,image/jpg,image/jpeg">
                    <div class="info">
                        <input type="text" class="inputTitle" style="outline: none;" placeholder="标题">
                        <input type="text" class="inputSubTitle" style="outline: none;" placeholder="副标题">
                        <button class="add">添加</button>
                        <input type="text" class="inputUrl" style="margin-top: 5px; display: block; outline: none;" placeholder="链接">
                        
                    </div>
                </div>
            `);

            items.append(addItem);
            editBtn();
            addBtn();
        }
    });

}

editBtn();
addBtn();

function editBtn() {
    $('.friendChain .items .item .info .title').click(function() {
        let item = $(this.parentElement.parentElement);
        let info = item.children('.info');
        let id = item.attr('fid');
    
        let inputwebIcon = item.children('#inputwebIcon-' + id);
        let webIcon = item.find('.webIcon');
        webIcon.css({'border': '1px solid #666'});
        inputwebIcon.removeAttr('disabled');
        inputwebIcon.change(function() {
            if (navigator.userAgent.indexOf("Firefox") > 0) { // Firefox
                url = window.URL.createObjectURL(inputwebIcon[0].files.item(0));
            } else if (navigator.userAgent.indexOf("Chrome") > 0) { // Chrome
                url = window.URL.createObjectURL(inputwebIcon[0].files[0]);
            }
        
            var image = new Image();
            image.src = url;
            image.onload = function () { 
                webIcon.attr('src', getBase64Image(image));
            } 
        });
    
        let title = info.find('.title')
        let inputTitle = info.find('.inputTitle');
        title.css({'display': 'none'});
        inputTitle.val(title.text())
        inputTitle.css({'display': 'inline-block'});
    
        let subTitle = info.find('.subTitle')
        let inputSubTitle = info.find('.inputSubTitle');
        subTitle.css({'display': 'none'});
        inputSubTitle.val(subTitle.text())
        inputSubTitle.css({'display': 'inline-block'});

        let inputUrl = info.find('.inputUrl');
        inputUrl.css({'display': 'block'});
    
        let button_group = item.find('.button-group');
        let modifyBtn = button_group.find('.modify');
        let removeBtn = button_group.find('.remove');
        let cancelBtn = button_group.find('.cancel');
        button_group.css({'display': 'block'});
        modifyBtn.click(function() {
            let newIcon = webIcon.attr('src');
            let newTitle = inputTitle.val();
            let newSubTitle = inputSubTitle.val();
            let newUrl = inputUrl.val();
            update(id, newIcon, newTitle, newSubTitle, newUrl);
            close();
        });
        removeBtn.click(function() {
            remove(id);
        });
        cancelBtn.click(function() {
            loadItem('web_manage');
        });


        function close() {
            webIcon.css({'border': 'none'});
            inputwebIcon.attr('disabled', '');
            title.css({'display': 'block'});
            inputTitle.css({'display': 'none'});
            subTitle.css({'display': 'block'});
            inputSubTitle.css({'display': 'none'});
            inputUrl.css({'display': 'none'});
            button_group.css({'display': 'none'});
        }

        function update(id, newIcon, newTitle, newSubTitle, newUrl) {
            $.ajax({
                url: api + 'friendchain/modifyFriendChain',
                data: {
                    id: id,
                    title: newTitle,
                    subTitle: newSubTitle,
                    url: newUrl,
                    icon: newIcon,
                    token
                },
                type: 'post',
                success: function(target) {
                    if(target.code == 1) {
                        loadItem('web_manage');
                    }
                },
                error: function(target) {
                    loadItem('web_manage');
                    alert('修改失败，原因：' + target.responseJSON.data);
                }
            });
        }

        function remove(id) {
            $.ajax({
                url: api + 'friendchain/removeFriendChain',
                data: {
                    id: id,
                    token
                },
                type: 'get',
                success: function(target) {
                    if(target.code == 1) {
                        loadItem('web_manage');
                    } else {
                        alert('删除失败！');
                    }
                }
            });
        }

    });
}


function addBtn() {
    let webIcon = $('.addItem .webIcon');
    let inputwebIcon = $('.addItem #addInputwebIcon');
    let inputTitle = $('.addItem .inputTitle');
    let inputSubTitle = $('.addItem .inputSubTitle');
    let inputUrl = $('.addItem .inputUrl');

    inputwebIcon.change(function() {
        if (navigator.userAgent.indexOf("Firefox") > 0) { // Firefox
            url = window.URL.createObjectURL(inputwebIcon[0].files.item(0));
        } else if (navigator.userAgent.indexOf("Chrome") > 0) { // Chrome
            url = window.URL.createObjectURL(inputwebIcon[0].files[0]);
        }
    
        var image = new Image();
        image.src = url;
        image.onload = function () { 
            webIcon.attr('src', getBase64Image(image));
        } 
    });

    $('.addItem .info .add').click(function() {
        let newIcon = webIcon.attr('src');
        let newTitle = inputTitle.val();
        let subtitle = inputSubTitle.val();
        let newUrl = inputUrl.val();

        $.ajax({
            url: api + 'friendchain/addFriendChain',
            data: {
                id: -1,
                title: newTitle,
                subtitle,
                url: newUrl,
                icon: newIcon,
                token
            },
            type: 'post',
            success: function(target) {
                if(target.code == 1) {
                    loadItem('web_manage');
                } else {
                    alert('添加失败！');
                }
            }
        });
    });
}


function getBase64Image(img) {
    var canvas = document.createElement("canvas");
    var width = img.naturalWidth || img.width;
    var height = img.naturalHeight || img.height;
    canvas.width = width;
    canvas.height = height;
    var ctx = canvas.getContext("2d");
    ctx.drawImage(img, 0, 0, width, height); // 绘制图像
    
    var ext = img.src.substring(img.src.lastIndexOf(".") + 1).toLowerCase(); // 图片格式
    var dataURL = canvas.toDataURL("image/" + ext); // 包含图片展示的 data URI
    return dataURL;
}