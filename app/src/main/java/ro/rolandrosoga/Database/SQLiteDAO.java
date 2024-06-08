package ro.rolandrosoga.Database;

import static java.lang.String.valueOf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.Nullable;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ro.rolandrosoga.Mock.Media;
import ro.rolandrosoga.Mock.Note;
import ro.rolandrosoga.Mock.Tag;
import ro.rolandrosoga.Mock.Task;
import ro.rolandrosoga.Mock.TaskType;
import ro.rolandrosoga.Mock.User;

public class SQLiteDAO extends SQLiteOpenHelper {

    //DB_VERSION
    private static final int WTM_DB_VERSION = 172;

    //DB_NAME
    private static final String WTM_DB_NAME = "WTM_DB.db";
    private static final String passwordString = "UxvIKJ7pz+40kWAC*N<q#mh5CfuaOdG*%gnZgjVf#H0@Z>Y8/GP$l@Rj$8V$ggT*DU,d;l,GsgSBsK0LpH0;c*yUZ+|yzhyCeQwim8cV0&j/Fk|!*UBr/iI&=l*qk#dmbmA*g4u4P=-z!128kWbqqp&P$8WK%-7ROKH5;130n$82UE|>fo|IHOpbB1JeLBNi%2$1woZe=JO4Py9W@D'f,kskonB0w'37MSZQTar<;IOXx9x2>UzKjf,rpsWp0,JM$-S=yLZk=eP/AQ#9dTweO*mD2pPTlL79obi,*ISvAS+tr+<Jq|&#&3qRiE5ce157d#++R|r'Re9OwUHR#rZyP,/pO<F<@v;JAb2SO6B86AsizIEl6mnv-9>;itZb9i,RPwF9Fs$pE*T=IKws/5gXI6aaS4g3P#EOk&lmuYF6+|BTIxdj!0bX*o/vE|1Wxdvl2xy>73LF8iprKVtJ%2R*iuCtCPgv*369W@AieGfqYyLKXKd>5JEjtW0klFX7xQs#fp8b01Z>BwPl-Ie621/ZKt3-9uPi!yFUY-Hdy45@3kw+RDsLUy;tN@GhUCuP3!/9zZ@5Auz7Ey'P4v>RE@Sta>$pV5KcK&JU6$efyd1ca>'f;d%pfxOi7A*jh5XmXOgAGc4fci>F*nOUUf2qV5MvBsD!7c7@UeNDGON='+FsRPJ=o4F5fokfYd93z659pU3CuKsOVORicuZUYmwuXm1%5Wvopc|FD7lw;yp;m@#B2AXgMBVkN8-P-FUUJz*E!X$;agf@hN1K!WPWo0SAGVN479JnS4&n*YArD&jC@R8<c6w%Na7p#LvZ,q$fpdvrhUBc9f7smc2pnHyx@Ry/*TSW1,SPS3h*i62k0mMSE78KLD9ynlp;Dj@|5KpImlD#!@>f/+x'QH,!kUbjWnKsoub0J+>SmPK=YyBwoq&g=W3MqNnBP>A%R$M1pL3lWE69MJ$&Nb4OhT|BtwTGAsodOWSz2bh|wNExXa6;u&84&fr<aY&V+2kNEvX$Y$U1!=I-uR+u1roRE8QCov%U@|6PGtaormqVOSa0opVrX$=JO+<u,$XyL8Y@Y9h0U+8+sEdLBO6Q$;#!qqvD9Wp9S=4>BH,%C<ETWl2q$G9uV9kAHY+xzifPlibvM|c<FXjTYp8!&g=4Fn59CV>XbqNZXQNNydJcHVD7HHe=hhBkme2YqXmijTvY#p|QSK4$o>=#rYUDG9lU<'g4@lV+F@qwa*phwKD;U@0*VH<Q<oEa9TRkslfZRkdhl6z4aa9%l5PCbC#5#aYd*yYzAGq@QLQ$aafiKr4r,Mdgte!D5h>vn-;T6N1JdV*+HEpTk>aTwjYeUEx5!iCjzvE2>>Yw9%>!JqWN!G6,ahBg=AE1Q,F|;5|=FWtJ2=n@#eDz!yKAHGUmlG%wgdO8Au7Qw/%7326*ts=V/ur&UlsqE|a*4Jmt+%gFYr&;-b3Jh5eJwvQUO*MXtM@MKWSgKMUvGs<i;LNF@k60>Z>S#Oky3>giPa4Wk7A/P332&;oe;6B=Rw$bsG5xgJC'R'rtYw1f7jD;Lrc-2wG5XcPpyfuUcaUUFl1tIwzt6xLU!;kZ#7pU@fkByfBX'Ix$4=%#|dlI&J*8Q<SQvWU4uJ$vx1z4G&K6t8m*//e4&0ou-pvwxShW<nMtnUCjULPpRZe4!8u|z;#eytY7N'zeDi'Nb5cSqsK@!,iId6au%x'pX@BGbcK-j$|ha'89s,ez6CKUeJUwd|x7SYI&h+o$$*2nVW6,|EnuX7c5ErM5%FaApe&3RTxrb0x=zW+d8emQp-0uWp29%%@ot<Xt7=hn%uBufG@54V3I<W9+nZ@/Fe>K&Tv2u2KrOk*SiRsOlDE'N@8Z*VcOvh/T498rQ7m8-x+gKLKEK!UN9,tR&5i>W,3W,MMP31cNyZU#bz*Zf@XSmL8/@uv@L+RPML2JJWmf829LaSeW$+RnwEi;uYjh@PZc$Dihrjkuc2Z6N'7U*mQ|FW#8LUtz0emN5PQ6BprAg-&dN+PneG4ImP1LC5CgTWd7$168UB$n</NYOdC'1yv%GDuUTV8Hma!&h9MKA4p=h4V&N%J6FoS15&@;O%iyGg#hStx10Sq%f@<|w!H11D6A!Gk@z1*U4n8121GywVu82W=B6rv-f|-U@STO;OgGN<RBN,SI,MQkko9mZxrQo32wKJ6YJD!LL4d8iq=KUo-Xa3XqTd-+C-<73cNTC=Q81ncXl1W&Nu%CZNw5z#&G5'Ed4dS@Pf9UVMPv;7bXOP0,CV7=AOm6BFSoH'9#<eck%'<ApatpK<;$0ttMl+WrloVd,TmZi1wTHs#t-2IK;cF8>xGS5eh<SSsi/JN,7mE>r,&8W<-Zcxyvk94US24wgUTzXy3l8SZ|f;at/D8X=$%Kl5nXT+&7'M,Re6W'-VsmzKek+%<'Cw$GPWXMy'ThIbQhKT2%!yH<ocP;r=;z>K&>,8-xN,10VmCA$!VzeV%Ms@5Ee@hpEh*PEb;Ly+bqZrm+8jnr'A!mNiftaGgPr%d734+DCTw0!%3hfAk79Vw&kznTW,=DzPrMT5!V!>Rp';RuP4ly|UqgdX-fb2gmM*@VyeV'wAsB2s-6'#F&C$cds5g-XLouOQ#u41SmEctY1n&LIlf50sPQNlt7m-HH=p<NEy!mn%+/uulp%H'Y*zlX5PAwmxMRm1O@Ukr>dZGliYb*2!8Gh5WAtDGX=amOtmnnBoLEbylsn%!7fTVQM5Z8VrUc7F-9uoJ6e86@sRz'YWui#Cfym4E7uAN0W=|h'm6on9tjQ>zUdc|d@aGP1wBMctlEkkY=UbO/gwhwvh,%0tQzko!y9Sp=;lqBrT!*NAZ,aVyhQ5;N2Ka<+Ys&-f;So<0$Myp5t5pcep!'HceW&q=+m1s&ZK%wYJIDm2wn,04OdjJe8J;vPzg/WqYN4%tG/2108JV'p4N+cjv6|I#NLZndGZk='PJWlgVXD18$N7Ryd-RI#HScJ|O8JHbgMEd&*3,E!rPXen1GSTIxbP8WJQOEj1b1TCuB%4Cn<K>0A00$Dj%,sdO@ZV+;ER@4uZqRvvkd,;rju&|;'VfbzO3-ibEuWEop-pKzlA-7XBc&u#x5#;FwC%FZKi4Ia9UIGZ/#ayhNT0Bs>N8|G|m,8L0;Y6qu$QR8M25-%Hgs&XU'H*>E$|4q7dZZ'fEo|CT/PIG$P-iFhl&=dC|%'izaY24ck56;P/8ybSSGaIeYpf8|s%3cO#tXDl<T+Qo#<r76v|Y*d@GvJ9LvsuKiDwwS,/0|Dsaf1E'N%XMI'SXTmfJQ5isyQ#8|Xe4,q|Yu1%1TcEDCAAf%9Z#YdW5<%G6cL!Po=zKtn/SSWk/;dKlzPVm$d-Bw7N4GjhWRR&i0ubzzx<T+FC+FgZlJ@nT,;U/hRGNRS&qMc>XQO<S$j&|z!DwaKQ>@tQ2l&/Jf0=RIXkw#i3ADG$HCJR&!#$AU0;ZlN'/uRF=!oe;No%9gxyf/et56Nq%&$HviR/WljsHvxXLv7Q9OHl&x7Q$wL'VFwgS&2!Jxt6fo,PV!cGaEhyj!b8b7irw*a|<<N>XK2>-gNeKdYGe6ilL7&wQ9+VniFc/K,NCwxIb2o,t=Ve&7PmJFfl-OHIJ$$7oaH5x*K8dHQ#d0tt#-,jEvfZK<g%/Nvm-$njG@Y4&aJy1#,|hZrq%&0APa3%,=bB6g$lrG6s=bE4oBP<i-ij5T5ZVUVt;$ZF%yeGI5TZJLWSb/bU/Z4'ZLXV4Nf>p#ePbH0tSQr4aP5w42L&U>dUd+F6NL60j-Ei;jJpC'7YzUJlIqtjm3kc+N4*=#oRi8Y&4-awg<l!0Wb3kVx'$>'ETEZK>$3VMW9E1KkTB@i3i3KDHGr2YIkF!LM8=$IQ!gPK<QAr7=m4Z|yeIx0-1Ky/X-oUCXi<j=*+#trx1mAzq9Cb&oL1DLWAd7kvH3@86<Mfj4WeY5C!3WwZ*#-5#hcmpdY58qHwuytlU|TxQACn*'Z8lX!ri2x=gwk2R>T8cov<ni*-+5Jw,wv<>7|aiN|*7baYQ7jaeSF=I-bx/t>T|0j&y<TvRhJ3H*#EztMlLe=g9Uq*juzaXuLX1;g25d4rmi;;0S!UxLck0H9@*SY0Y2'iO--3oNsIQ2QdRCfEkK!mfPy-el3c4qzeOyXwjt,FOxokirtPA8LDuSK2MZtb4mNspK&Taach==s-gW+gbi#a>'TJ'=EMreHHG!pLE88Tex!j#0,L+LPKZ4&GGHi%hJRqO8|AeAZ5+!Df*kunuFGO4RjPoMW$vtr>TEZSxe<$qcDbtJ#Z#>ZFAG%pc0BU=$4xGW$@=Bz2RNEi%hVqbkoOjCucUUF>9O@zueJUCBRZk<z;/3HrAvh<NlqvDnctY=MIj/CQ#b5b'1I;DQg2*CDB'sNL%4s3!*TIda+ttc8wCUhq2nhbN4H%fG@hP!04*VBnc<o6Bt!6vKkqhb+3@tHjJAC%HNpKB+o*wcw'XC=wEW@/LJ+XP-1</Oc6MjGjV&6sgA%fbPgpKyhJ<ExgNC6LwCpWu@Nt3Jep%95izaNS*XCp&q'#!-,Bz1&h<H!m;eb0@oDE3+aZ#Gt8I6wM@D!USqelcArn8Y4S#@Iilya-UtYA114g,kq02>cHFia+u#9PdIQugZs!g&'/DX/VFi!w'F>S0SJ;=mmV9x%dogt4QX>0<HgRcvWSwa;jpi+*jrxxXU+HAmC*<F3J$jH$kqt/i>dcwjveLzr&ky;xT|8jNaeXoGo5IhH%5HOsw6MwMwHm*<6S7sxs<MT0qa2SBcGxHaVEFKkRk'AhrX=RGZJTu<E<R-VB'j;DyxS%v>;+QvX|Ci#>w/6tn-9$CUCZryPP>Q@Yg-azjSMQN0;UX%@G2z;Cy,t/dux-j%-2q!1s0sI<k@VGzxKi,X=ExY<MQH'cIP9'ijU1D5K=!!e!brk8/TslNQsJ6j-i'5kacw-9Sg/$,C/jT-q&dY/9=j|LvDKr&F4s,@bcTHxnSEjK5hbnJY&RQO2Nc4'#50k%2f9;o&-UEnv$CuT#Xdl|8XzO8Hbz#|@;;NgF*,-8mlhWI/,F5P;P8*21q'kE3Jl,rAt>>>CxxWYYn$bNJROI/v2RDf7uGvC+gO5iW5FDb7XTiXYE#tp|qyTC7>i0Om0qlR3%zhSqzTMZav|pffjKl20&;vijtga*jLO9v1l>T0vj3f=y%,XghhO7'<-J0<#bl/xUzK$FY8,3&R6J02+-PI6#ZXHA$B=Xr0Wjeh4Ar6K2cWU0o-mS0*mfFlXruTIOJpH5m+f'VxcqRASWRBJ/$IlSTPP1qSO6>*KaYodh;%x/x6U+5TWb%<c3qENGwiuCBm!/jk>ftDdV<h%bvRKoxOWn5jc*&tRXwi7PN*7HOND@5oLnBWFT@|Lmb-##w=!$TH9usyZainvrymek$ZrH;t/dv$aOO!nPa#2VCq!RjYdsZE=J@upr/tZF7=/#T'HEZi=CiG1jZo$A&eG<J#i*/n=uKURLaXc!fViB29gba/&5hfCJPjQb@;$vV89oJATnEm&mV&=Dy6iV=/h0zvPX5GM'DG,y0v6Hjnd5+IN;H*N;p&#t-*=lXFLX7X7N6etXPEr$A<wN9+UbTV9<I@1IN$k=>Nam5tYGMe7Q-jt09Dt;Nr0Vlqac4BWLKthN5@u!Ny>A/c8O5GKh#Z#j;#zslH%5rd',c7Rj*PxmJBVZf|wAq7daHTo*olpLuwunHmKnrwR=8afq'-RAd+0=X|+NWATXDSjSqx5mB&OP$ATDZXQ0X2WtkJf@bV>BP7Z'*t1>N>w*rZj>U,C8>'mX>cU4Q0LU2*!NcAJFzYu1RILJsh$eNK,&<f#o|ixX!SA#a0iZQiHkez1Bl=f'he'LxpOnmTfA5dpr=@+=f&5;jDAiEMzTNPaF1<OdC2HI@iycAsYfUQUBx5<aQCM;y4BvQRpOA*ZTZKwE*qvMx-KaBerz|K7!&-D3Jnr;m2,Ah/cwC|;nlktMB%c-<uq*UG7<EbH%&J%YbaCd@GldsYD0EShIeJMQVKjdN+-8=;HyBhNPocbB;&XL@w0fFRTpTCp7Q!6/QW/ax1B!Cmc@f5XpHQwvxQ!Xv,/o'xuF&Q=,IylMMpeJgOMA@sj6n$/0hJYTZ>Q#Yc@rR9&p>E9cTkr<>z#W5tT8wyEgT<-Syy|S29puuJkuc9saqAFus9qraS=/Dt42gQQ|uD8Nxo,DUzxO8b#SuP8X#=Dpz+s#dZA7x%T#*K1Xzp%i,/M7D/P50>Cq*Z$tdam=!SXXI'KWS+8tp|DIK/dzxB,Eui!D$7/;ME6aOp>&|kiZg;X/-+/DQhF0H<O,3jKS9=QSSZtu&'c!B#f!tiJmX,wfNE<Y6/jRh-k7|H8h|4uYa5FM82#@;ct0lyIOwqzc3H*,3l6Ug'vLo<*-M*w=svPj3M+MkDKL7<IEYqnh&9LSb3gsfq/pjJb1=%SLpZ!Wj#EJUC+msNSBIFrqYaW#j@Bkt2F!OPenetM6Oe<D;|T<>q-5gWQ;5bom3a$=sWF9WVpA2wK*0uYjG-O@dkE@a|y%,ADj|kL46G9>$$Iyy5,|pHP70KX7gVW&OOP#/G2UwY#,,=qfDI,OFAUK483rU|vdu%IaG66ZCjc4sCM@lNmLl6BEvc/LMppAH<kI%OVA-zGIiJe!pf3Pk#Zv81ome1YQf,F;dXOzhYx'/a=9dfgCHn-M7rz>ziF%m9N1O8SLK#YFMy>@vGo@g&H!|+wvPp2VIEL$#<DFq<V6;s#+BV@B$%rG7&>O1swq%U7yVxvvoGw4W1xMWa6b<sYSOO'ESGQGLwA@lpXEdd+A@w&r;rSv5!nJy;pX*r%utKIU#@8YfXYRP@JlWlDdvEl<wuL6SCUQN;#v=wr31dSi*/fqzght&Xs7Ac,/%QU+ivlzVc!jW!5,x1b!-<8%2BSstgQSIHvwqrT;bb&pF#wmOE'8hI*6%#JJkD<iR>$;BfuFKu/bYwLMJ#Ss|P3S,6W!SRw!P4a;XLVoo2Hw0tLv;wjrOwwUI|b57neCgoLImFX2ovmi%Tun=R&SVmUyV+r<5SPrlDB#;mDeLWx-GgkAy*Mu!Q!<R!Jw5CgHa*-kfh|IpzU,=UVKGm!OMH#@PFdP9d7gYSmu13Cd|aiqe5R#uB+an9FfCdg9ra,j/DfPevI0nq7AM@tGLlI%=CxLjY#bdiS2O7JwOab1C&7&IY=tO@dyCo>h69Y;E@n<pBw!ORSS%T-1%Id+>>nn,=qMqqnB#T9$;obQB-V;p>yWO<d>jsLh32s8%Ruk#zba+-ON+eegPhSMoBYPYgu7a0-mkh%bb&AJjZ6wO5gp6pn62IzQwtzi#'J6CcI/dS'y4'bjx|y,RZQI's740&'M70z34hVHJZe$nRbSafmBOn3|ASXJa$X2x<KrYD!x5xakmW;mIVaI%eO-a|=!7K+<D%#O7fQEFiQqK4K$wmzo/X$/2'$g$$7S5zwHQLC/=N5CO!B@C'u3b+zUeU07b3Ie3LPU=C1Ejmx&Z16uzNz2MprbisUC'K/y9X#@K3=H<-wnwJDtT/gs>gHw<6&O1>C6PVbRUsC#Zu$f+FSaaL1hc%KY4szDsfIL!rfI5|OxhXZ,#cLKGigGfZ5'12*0wve8JtACoGlYEOTmbxplU4adP;4%e6+=V3qT7HQCHb$rM*Ke>IxRSXT$|g@u=uV<k&PV<M@PmXtXRfJXYRfgA!URE&Jy1ABJ,c-8!K%q38r*;MmJnTjlJ7*dpTmyH*R/mJnQ7r7ENRgOA!r$<Wzuci=d3HQC5sytCSKU=;$3HrkiO9VNH!F=I<ZTzR4*w,&aH<TzKuSBvmBgQjCmr8@SAExoQrd/7Mpr3/Cz'mZYkWo&<VMT,shKJ2/etk+Jxn'HW9wZK21h<5iG*D<w#F;-Y3ORaRX2c6vlY!&-,SwHc-U5kSg/5wrusIz1Y8S3sEQ#iZoVt&5<ktEJ7O'kbali+TB21=,T3hYbdykN/>;h167&;jGiBd%aL&ddjT+Z,<o4X>Axm;dyJVrUgW*3S6QbuYKZHmYQym-%<zEsZsQJE$pEX<Se&jNle+H7!xUe8w<NPL/0yQ$3r*St'SJPkTv+H<F4I!MCre#43Qz4F'y@,o|7$SG9VACW7wmttN'=O=0ztpzY-bPWr2n>1%%+>,y@bHbsauOmE;$!JAGZP$*8s15T768eE2cpWVFT<*+4u@rUj1clXAtGi3jz2m|XxiuQHmvB6,SpK#KVKuQXxZ!<ZjabdP*BGDC5<Mwr/;k<U@Fh&-,Cvm$9VTg47z&wr<P!@B%9hj2jE;-S7VwwlKb+foIc7L/+JmYHubS|l9LaBc-AaEcx@UeB&Sy9;ne/P9U5M0CgF',g=np|a'BXdxkDUh,-SA!TSqv1$PWUKsJ15n-6dw|,jqb0dkS<vti7J!SVWENddG6G2c-!HLQ>o+Se+D/=bFRL%2;zZvBZbdo$ucb&lX&lGiokw#>mg#c&x@T8g+=mi8JDIvsC/*rh,ClYBUhHb/1%@37GX0Z&Lx#%T+H#7&gqe0>,x/9==$uz7fuQaPhFHAHQIQ8Ab##P+-+DvM,>Iur;qE>+-#qFfJ*qz|&|or!n-NkUgFZ7TPsagrpWspA%Gn;$j2&-@DuX!-Jv<n7udj8wQrc0nJZxJYid;M102'*B253-nI<+2g7|-%+4olNO2%bb2b-vJetl/vH$v2L3L%lAtf|x7BEOP7OyqwCKfOJ5wz%QAT6+kW'M@deV<UobI/ImX>uot!XUzkPaYNiD+Knc$6tgDR>dvp2'-mYzY1BMQCytcM6UHnwpiCm|!C5O,rPAo7-$0F+nG6yucny;g6c@>l=-=i8ZJ'QzQaMUPaUAm|FvG08LZyv/S'UVzhk1w$zJlY2;DHg'>i<w<;K',y$jgs3AOjEg8ZPNNY41VSwT%Aq6CvY0MOpFjfXS#%n5yO&fM'4Esq$s51H8ojb8o!HXBRFUu@;NqrpCm|6ifv2*G/LO0KyN9CRX47qTLJev*N,Fi7x7v8rl12ANco294t@=gWFcmUSxuKz*,W6;g&akkSsbZV7XEY@m&Oxq%FQjzN4i@$o+1HL,My|I7'm;%kpwSTyCGKdWLl%ReD85zK-0nt%Tb1Pjlpi-$&Y!FvQ-OwyKjW+-|;k4r82|Im#Su2st<cAn@Qe84z0wEC+vBf,gCGUk|my%oNbpLTXCJL<f1m&3Rt>R<B/!diUpb<Rekp5h+Nltq3tpwcHG9a$%O'x9RLU>K!Z/a$vwEiC22zef2@ije<E=EpY|,b$>T++c=Bl5ARq&ConzMXihoTd5-qlMnVhT/lh!NNYdK,,V=7iMS@;;XWUjN0w!ufFN<Rz6vc9#H1qhl,JXW|PWD2dlzqr#Vw@5zL0FRQn;k8k<y<tbYt66V&2rwXy1f@VPykqp82i-loBQLSSQoKTZAGB7Zj*lF>u@q0'EXt>5d,89>NZ+WyF7*hb1b@-vX*@0,eyu=l92A<3M3@BJZr7*yzckikhBjeM-<0G%&2c7i/w,=etRBA&H0pnX!a'ybS9j6,cXhLvGqHDONxkeBx|Alg*fho&>Q,cyd*iEjnJ3Z!pjL$tg>Afa4S0IV;-H-jEPs|ZUfAqbv*@j!fuTN0jm=dZy%Gk>tLbMqKh02hOs%ykkcl$LuuSe%Koix$CT%k>@>$H90tTBewb;wnRmH7EJ1@Wk9#zyASXk'Q;E<fg;&i'!+@P%aK=<5Zya4*B#>25njJ8DL+w7-hGm5xWXAuegm/1C9hRtAe@ZEceY7iapg1wWd,yiGe1g1a/<$;9+4E;w,f94toe!9-tu/rv,tMejXvgGDkheYE3-f'H*J410G$2$Ibn60xTXyeT,Wj1mayW&*SYk%RiRhdPQ<*YqKdT!w*GDKvtLyCn4X4!Zj7I3=SL>Pj=GINV68>N*t;KTphO4/@Ar3>tKehKD*KUrb,r=Zd*E+9,=-CC3i1Er#D&rRL|>0*,'qI,Y+Vs#tUrw2LBvbI3Y7pDNBigZHx<*HjH#6A$Gy2bNLjT&Um|y,dt%d8pOn1tfVZtLfV@|6i>4h|A7$w/F@2JE;RQ<'JZuo/#U@V/%J8qKriYSESRojwDUppOYFLChI%GUA#||*N7=-ycdJn-x|4=%0zmOwC-D=icEzjIu4K9>XG='bWrZW;f#8DS+xwUs;%/PrjSsMHjNOU52yJHXJmV6NtTLg'lj@Zi@ES#M&A!hB%EJC2PEdMtYQtX#M'Qau3E$3LSTYtVPR%OI0rb%-E4>GLUa5tsk=d3SHgp%wDFg4nPhG2EcP92YgRS7k&ZbGc#A1e*ihjjDZI-!AdI7w2M/pa$3h4bXAV>rbD@dObCVh>HZJJpyz#jvBC6ubi|2N/rk;j*lE3#ZpUaPaqtXwe8Tf8knZbyLWGyBZCORM'-r$izVYfGKz'ixd;3#DL4XnY2Kll9jjW#3uZydO<WcW0wQb>jYT|lHChtYCFabMiE@d>MLMC/%iBl>AJ#tv-FaITC&$z<XX;KE!%4WFBcw@FvOH78fP>gc0Rl>ha9|x4b/w,E->2efj!SoKp5kpo4%2R5EZLoJQ!a-id+iskZ09/v<B#eYv#%/,gnGJks|MCcOVAWNBRM-2cpJ9DQ8eP'vSb;HmojSaAlz0KJ-2Hoj6k|=;K@|uNoi;GJcfM&Ou068dwoNAgXIIXG@/rG6OTbTEad'@>J$aKg-Cn3SwTRKqRT0NS7R=O%$gvT7HA3XrxW;bH9PX1=aEFgpKB50&bFu,iDAlbih|1OFLtm3o3K3bD@Er9DD2W6Lo%yrT/FCZlGbd7G9AoGN01|ay6x&>j@tNRGUy!22W|GfdMn@''1*/&d6/y9BjR+h98&qiMrVjvF!>M;5cCS$IaN,+2J6M,Y%q46loh!ca+l#xMVk9|q<0FVilkABVy3O4BQZNd/0zZBruCysn=;'s,|y@JKgJ%$P7v&Eauz42d+0L=mK*y;-/@HHpxmP#!uzANJ*h>Wr0&d+qE$tDPEG5va3RZU'U$'+bnbag1AgeY<gB;CPBJ6sWM1|=8I'z&Zoc3sB5+L@N6E2F-lI/F#'=SypoJXH9&dj-vU+=5!s8Fg3BFnU!8Xs|nVrk1J*cstLfCYwK++s!/SJGFe6'67JFA'M@DGm,3qGL*9ewFvxyx4F28G/*sTg09YsM<hB>WP1AOIN=ZRAyNd5,l2Z>mKHyoWWSP10BzldDtHb<;AJGE93<;FAoz2bDibDA6'38Q6x8X6J&8/cYl@A1==qt*iscx+!wd'i6zV%AbQ7c16Lo!DdNQAIrC&LC3<bbakw/0z'naLE'EJ8Ien=fodwO@D=Zr+1sn8371$Z12GJYG!hMjVX-2pwX1=dw$6XIonndaADQB@2*1ZG6,HE|r6g0NDI#L%h&KoVZt7+Lq$%tL!53PLQ=aq0WPg!iC+O+9SsGE0dF+#*r-!/f@14F4SEMozjGArwR8HIV&eUFGhh|/8m8w8nDR&192yqM|N;adxixLq!nvx&<QiU@SHoT98T$AM@#bT<WBYJm6w,di%6Nu1pRb0XZWE4gdo/uldg9tAv#uH2gZ+CE'T4e8!TExT0uSKlrIT8Y0nYpwVua@EP/3Bwy9ZGBwWy!Yd@9TYFE>7X&cY#hG0qwgAYoA!D;<bY,p;*LFHG|pgcccyJaN=aWQ|6mj%4D3wwgYUs6CFkRwiW4R=sjTzC>Y/u6a,!VqeG$>SZIs8uPokXov@,g3|nN#Uh5J<+Hs=pQVRn5#h!2Y3VR7+5,5I0ur*kfyUroPzPU+@2P1s;oV&6mF43,9&2tDVsGUuONkasl@bBh|Ic8qu#GBv+wpaPfqCh29<t7=CeLogvq#xW8JRN,Mi&bggGR3l9kppnHsItj*ad*uv21jgUt21Zr<hBB%N4gY+Yp|/amN|XBzGhpmZ5uHnL3PFktO&YX|TapSnK=R249nY=$r&ce8aCJ0L$2WvQVzvaz58UyiK=;DZAHd@9>!v8uOK|zL|hI0gI-,WBLVsc%=UEW4VKn;y-SscN;5R%EerpZxa+pIsXKJkC'RwnoadK%o!3dVRm@gM$7<fTK=OxjF9#pX2Fe9jY83vt95m$qw%&3tG3CH'R&4K6;D&E*IzCT5HBVV'OLUWR$nIGei!45bf|sljZAQ4CfllA5!*l=k;79Mcv+-S7PI8<hp>V|69bTQ$C25extQ;-gYR556;BOC6gZ/+nKgDRq0/qb!CCzbY';SB>RW'Irbs1hJtK<*GLw|@,Go,VbxAYhuz8uXaIHBY5TvaR<6zKzAYp,,M%$!&5kfmf1&k6|X*Je,UoW30c/;<E7Dk@qv*7yIXV0K*AB1sYQylaV+2%!&IM'=PX;e8O#$!|r1b>47FpB|6Q1aHcoAHf<1HHF0r!zAfXDCTvCabT/JVpM%@|JRvIpl1TaoKSo5c4s#g%PQ6w*@&bCtukVLvCbrLlmcH<MoHRy94xSckeiZaQ5N7ja%;uHxPVE3Kz-U&6BJbh-kk7QiNMuWTZKdCGMW23#ff8Bmw+;zYglv+%m>=JhGmIT%o97GhPK,0K9@Z2SZ,M7A;Vw@WdyANScvcjm'GOrCeSR$dQOvua%Rsl1fH#8cCuN<6I+pQ$m<IEnY*a|/SftsdreqPIs>*c5Tliqi-&S4u|9z1jwudmTr&jpF7PftvDWgdsyuO14Eo$P&TJRZ=hkohGpCzY2><1=C$0H7=t#w-If@Z7Xgxx;APbdmz7nry*IXvLw$K<6|z/7,1e2f*Y!N%yMuURda-qQ/tB%jP8zH422esZJ!qqhEXnRJeqEKl04AY%2rT2*MSY-waesC+;6dxo+/6Tug3=Qf6p%pxvwym17j!ix9O!iNNxB!6vYD2qdzNv19|Ym&Nyivux/9q3mcN8i,j@ix<vB!e,H1%YRTq|*xd>sSPAPzchiKQ8N3!%fX&D%AKBvt6ZoQ0Gxgu0,1NzhIjT!j5HsRA7,wV7eM+41B@aSJs2q=B;|jgTMlIWz$+FT1Oj>irbMKyL2+szQuizG6/t$mx<VU+@KHJx*l9J='Y4<pOtsZ'OMy<D#jEhSo2KYuffA7pcJ@OX&+w-KhzXbd&$T5YN8B*HTTph0ML94,sc1d>#I<<3H6BAkMcNkit|400>&%=yO>$1Gb%gtu>jS'VcLOtG'qr5fbx+T%a=mFWzR2>a1XloyqFNko;z!Go$kdloNUXf3QIZm+;Mtu#VYynC#Ryt+k>IprF$&i9MbVJm2e#z=hbr,nN'AQ%mT/q6A;*IF977TJm0hS%B3j8w,tJlKTIcLMAf;&7/gX<uZFu!G>>Wk7vO@,W9|zEmJ%hja>v=G0'#XF&pQM&grdpBg6Wof0/S988&q<c-;rBWN*MnJbPOb8|usucPxw#+D$wO'yXBbhYdoGnP'9jI#KVNL%Q2*ZJ#+63SQyx!mYO;>CSTi#3o/|K*!ma/G-@RfOBnHeRgGQcYOB;qhdpczZI=8ndwEL!nb+q!d4Y4a9+eAc6ZodDHhb6XMErh4pGaV+;!YXp5zidK|3aXCV*QnRDqJcIzBT%Nb7y*3ZiNg*jH%efpU<TQQ;A0zhGu8vX-|cL!PbpPa/4,otK2F@<q>akMPpPbQDh*ROdnbGm;;wI|L|CnGsA<xLcb-sUkW3KPcXQ4siUs=0sw,V9WI0pORE/h5aUDo+pAs/|lNiU4AzMO7NjyzfpSDGy89W|+j=Y>,hVKAD;'xM7k3qMtY7rGlG4QpVWBmIQ7Wz@ig59BwYR6a!2mjXEw7Rr1MIBG95EHO&f2DKZ%<Zjegi+,GXsmGBD%f@NKA|Bj&/H+dG4yGbxc,iD15r=2mR#rICHKe+yjgoU'gn*FzbV5LuXDZ7u8u,>Bk=$x>0K>8q;zfch'IP21!%ks@DKz8CoZXEq@6bzRRjPO|pa=xa##30&8nc5Ur3&#*v5SjGG'6sSwf7fxd&X%!nFx3bD#myXc+OeXr<UvGTvjVI3gBX+Ts8b'e2!Y<cB5|HsTEyZN*Bhz*<IQ2T/e*!$K!SA6=A>tYHvV$pXuLI|<&'k2O3f$eKvOEe#'gicV&SDiR3=oOpU$W;tPXJa*Qi/wbV96|77/EZ5/P5O!7ux0>/DB%2=u|dL5@=!!/vPvN!xe2%yuqj73$b39WOhzpSgcSM29ILV|k,IOTvu,VK%K=;snK|L+J0m/Pfp%r,29=>#hA1PRAIb>Pxdn7m,OvW5HUBCu$-MlD=Yv<!0/L+'-<iSCY#2&378jky-wlk&8FPm;&Jwo2wnH1EV%6/#*nGXJX!|rAg$VB*=4Sinn$YI3fuo*-7PQ06qJ4=qW2IT6e@z*-J62X'NZVxI#$VD-6'pE=k&X$!7$-g0VFljzoCMLhvX<r5s;JKD|gF8fUYf3Q/<*8d4BYo2p5c6@LFVkf+GPVSadyzV@+,cZt,YbKmhOR7Fuy-tME+iw@kI'541XblHvQ$qQFA=h#%po@MXH>@S$qoMHm&q;eGY$ytQkE4<Z5cgWAi-yIX1EPVmO8F4UenQ|%n6e+5sd+Sd!KEVXIkt,c;=OlbnXHa,i*<=u12Nc3LkDwP5%3K1KDKmGsdq-8EYmq*/YgY-I#QW5Ry#ZAOvVS;h6d7Tj$CuAQFwx;j<kdp7+Tv$Nyw4tJ4v|3!kvxcR6z%7xqzmIQZPz1G/&&QW1rBgMZpmtV;Wi/FGB-fmid2=9mE!h8-=VwPkANQ0nU,W9W/$o!SyCgb1T5udNRkar>5M/Xi7Rl2>UYDn7*qN!m9'YRNU'N>V#Q1jZ'<Dy6Rd*NLu;KS;DOHeReMsDn=MkYBHFkjiM5Mf!K9rg*5@sn=Ek+j-f4uB4H0W|&HDCTJrbd1N5eTCOz27<ja,OD/aLLyz$2Z/To*ssx*<<1DH'mTF=+V|S6qg|rHe*$Uq>mQX6B/=jXtnnQL+;<tnbvFP=,BdeES0*6cS$u<c&WDw-7s>U%w;k87N,%Bq/#&bz>x/77yv/!TmZ2TXvun5y0O9$X&7q3z/&rdn%iGMrCp7Q2F>y'=8&UIAvfyFpu!P/kpz9&UsACl!=cV0dWyUZ>R+ruI>|PqxuCfNQGNmJd7sG34l4mwkx95<93+KZMo1TsXce=4YmKpjzoxTY;Mt=U-AI@uS0>244O%4p8;tvI#9ZF'%W,hxVn'>>D-abN&HB5Od'ok!WFZ&qfg,9!#,rQd1a&pUaVqKm!|53=On9jz9F$ynGt*zIWSARb4l>wH|pR;bm>few'uPSOR7,wY;YICh=!QWkx7Dq3g&a@oHOi3-0/WJ557>rmt%&b+!q|zPjpg&-4luhm*e=zs%NeUo$MTLM#rEb@e!G'&p&T52/rV=8UvaMJ*dmKMK-!7wWtsrSU0rcnGw;-Tq%CFcQ9QUWEC-g,cA=r=*6!Ojd;uM45/;vOO=50T-'EMfj9WDAcAELwPfhlTc68mPreWNzOfe%vrpRYG6MLP3!XAzh$Fr3|fFG0menDh!u6MEg5J9wdXNT!ElsyhxiZJ9g!=cE1%l<Ebw7WDj6c'A&eB%@lV+PE=w=Uof!5SBWTmwfrQPJOxHDz%F+;Awp@-pJBy9xc#fYm!AsUut1ttLKRN%-$Zg=o<$t4>zS3B&SxeKBJC+wVjVp%>fqI>BpU+3XRzjeMH%RPk2=4tO!2FdU/=Z<GJi+b*UKi@2v%Ao$H&zFUPz<9%tn-MCdAYWec>&4%nsp|v@eDkD,Yh|aTI|WF&eVmJjun*|HJeXSvABGOkNN2KXtHpB47TJ@ahK,jY<G*gI7L9M68v74pGdvJ@AhKGR3,y-8d3WWLac/fb=xPTrkizHS%O9xv4,<k@XpD|M4&IqpjHQ&Luw>a7p5>!oqN9jr4Y>'U;;4=MulEKZt<@vo6U6&!i9%ugzJbXUVeCR<,UYbzGrMPSAW*OuLc@anLO8aSVMyrt/Gt'9cl'4aWZw3WrPL1Zhj*Fp5'Bt>dHxiPe1RF2%ENha=mqrDrkSrWlOucnn7Ec<**kh#D,gS,tr1h'C4sV3<xIb=dP7=W>aI9e=e@Jb@HTY<PjZ1a&fR+SkYJ&eD/,VwrOhJwc2@|XTV70r8PuPw2F<2CW#leIZdzwabQiybzh=wAW49cOiDPy'0y4!P5SNq+wuQoh'F<Ld&t;if$+&UX|J|-eVWO&itUXe1tlr7SnsA5&YT1'*dekpwJsHZ-v;4<Tf0Puc8ptmOO#I85XSqJ#qs&<'&!>8baF;0m6t5HgCDr6s-|DqE,*lo|6Rf&D!#FCNinOGSsSRwc@9eeh6fBWTw%C#;'0MBl1190DOiFrXK@B7v=1GL'r&lIynEK>uE#+MC0rv=UyktMGwBJW#vJaPrn4hhVrE2jjUK#eyk*dBkPpL2s|>'%0wQ|m7+1!rb=coYWQK%O;/U,b9hr,kSCoTWEFui0-dzsgeLiuq6KDscQsW<S8IZ'@D5Lp%ea8Uq>TIgk3LF&8g;g5<ZlvjWTNnb<qxPF3eZ0pF5VSZt%=&2SP=fIP0'g<ctOFSdcooGAOcuIG&QelD5GbQllPD#4=LudpRMutKJuvW-rC0fQsn|'F0zWjZt%l0@B1h=I98l'!BW9O@j5oYZ9!fF3FlK=irEB|0%9o%mXeuS'#yNVtHvM4O9&D9>,&/2XRBp*Y1j'IuU-U@oAHjkzSNu<Bjr++b8*QOn4bX*K<JStZ*rGEwfW/8RcNEJ9eIIIg|u;rsb=;Ou@3O%wNblfGh/!xh8!3;dMM3%3hz@4%G<8V4#z$h0ha+ELd$*PjZ$JD%5UjqrTEgBNq&&EuRh7L0$uJLdR6hCiRW&lkZTCuZC8P4,0*=|9vi-uYa|SUtCwqeOcRCsqv!Zte-ha0LWLG;WcEn=R82y1'ZYv1|4&Cgrq;A&;K6ECFMssU@Q1JP6XzXc-5KjHs;14|1AvT7VdLYU'7n;Y*u3hZaVN$|,7c4x9CKamTqVyt7B<b2U3F2Kv$%Ari3'adawlaV%=ljo2$CcPH!t3i'vc'%68Uu-V9OMp*WIXD/ms$ao0eWmi+p@AR%u8aAZ+-ytHOOSOS%IeW1PddYvdtMc++XuOm+ZKC1dr%#mdV=rsw'tD&T%0ZNBzqPTU2I6*eo'+I&De'=ymJ4Vz0rjRi,%NZ$13>>|EgHYjygz'eR9Ue6;s4fRb0x%$PUTwh*'J2V>=aVqmS6nS/X|0eKd/3939'KV4pehN@im|q=LYweW&l8i;uDU3uRms6w!QjAnDrzY<@GI42qa7Igq=-A3CN3cXdOLUdaiG;c!UN;UI<XYfLPcR@Hhi2zbPIi;%3Dn/j*3!v/*k@wCF$tx6%tnD/4yY56|kk*%UsYO|rh+76k,Xhv#v%<9sBIAK@Tv57agy5YKo9sLG<SkZ&'Q8hupmMj<RXnl'fNrkE&#3VfHbtjc7<EIIK8rkNkO'S9lS#4BJBwpzjz>Bj,%EJ5*6ORksb4TRv>4ZAlWy|'Qjn+m72yvFhCg-7esS$mpu8M25pT6@chWM6+z5nmKDxFG@@7<l=C&|U'wfeCVGZFWUC9m>pf-*SF/ReHpShpzMcbVydqAZ,Lj8Av>dLUG3p;GFF5VbUhYDv=zXKEotLZFshF#/mCqgYgkxIVur=WL2x&xy8tO3n$>IwLFdGX&4O6|Z=5x+ylz9XQ*bC|&FoORw#q&nPeJ$1aSW;*>&I#nJI;RaMh<oA>BDEBYmxScq>dUc'YA27S$WQWou|hBvgl*nk$ue;9vAvvn8g,G4y!4k+g<&#av-Q<**Lh!qK&#RN&&/J=T-AwSX'k#ZEj=Co&H/V46-/Js@yxMr=|&+zO@l>dwjww8h+ziaBq@4E,,*$/>fv#n>Qi+ea;VvhIks|#6=qh*%fKkb,53,%C>flr4CjCTUbP0+sp=x@w,Y;pV>@W!QfHimX1p8M'=3zpZ0I=t/u3+iOcNeK*=@opQmiOUO8lhG9ZR'UvtMBv+mBDvu5e1I/y52WG|Lhzz>@-Bvb4tuwdZ;J4;!L7|fPG!sLSdRrjQR7zWOMK/DfQoSJTtyOQPQYOvkw!mv2yC-SW3qw*!-iw%GjIkYwQLo%DgfmcKjmLr=&#W&WsI@2Ja=8i9fIw3yCkme!|iHWG6YkshZwu7y1$<Yf-4c@;eacx=P&&UM/xhG$hPqQ5aE<MeZw0M1=@-#2HoQgLM,-t0WfD75D0go!RSdD@iCs%oduoFHCiaF>o8wQv#zz;uII/biOM#Jcdzj;rbd%D<sUL9pVMs<$1>q9'bW9C'McJbRKXAA7S4<lN#pyKE$o;/yJExsEBh$'2URwZq&u'/!pMc4wZLIi*'fQ%=Tc%|,M*ALdNa3o;Yjknn!pu7mJfcO,%i3Dnuv6=4lu/&aK3z&ylP|!82|ub+ihMG1j!PgO&!UWHk1>x-'*E$bRVcAZ@ICH'%rxX+y6aOwmk*fyO@RMUFhAsFEHBH433%ESN0on4k1Rl9ppRQ>BwFRntbqEwB;u-1mL7xJH@igbgj0'%GaI%NF+hf|a%VuUDgYe&0l3wB7+j=-O3yrfH|ddbPFY>2#nnhuvRumZ5+XNwv5'eM>WGrnE'x<Uzcg#iFCKnb9@uH9ZVDTBU,&VXV@H+dg%E,DF7JWU<g*9HFgr#JfujSS;S9ckV=GuC!u;rRE$a,aPrrQ#$cSmA'u6OiCqm5*zqZcsNRyULN@lr9/%yrFzPw;s5vw0s<E;eNd$SpH>L8SNX1g,vZxv@&HTq67,xr4cRxVoVL00XV6yH8-5$x75kT1Cw=7Z0>=&8&dWK3GQpTg9j!;YRUI-LP#0ODsCwKZRxEOlA#,72AP9>Y$-owjv<HPB7Le7knB7R6,C<U#m;d;-t|KoIdT9gX/j9n48@GYRXI;8Quml$ZuoeJirHY598/+O1ITlcD<5J1d<B+FjLBG81cpu1a&1,>h@EGVy@xNya3X>DozOm@Lx*Qn0mrq+I;V4b<yoBaUAxu3nB!k/DxkCg-ru+*mI6!9Np;P<WN&IeZ|IaccBn%WBw$&O-'SeiAz'9!W@Fj14@oDf%16o@%v'6LVH5BzPWCU>S5sRlX-nSu0R2d!29ebssQrE6a'>u|7q;pBNU,sY@*Nyc/MrHo=M7jkx1-v@'!D6-q!z9UmwMQjV+h%Pqtf!M>%0|;Fc7R7xurICP+PO9&%fE2=g0AF@$>OiVxzj4|XdtRnv!*<4jCveljF$2Wb*K<62Ik%vw@06pKGN6NdaRUBqyMAk4Z0yG7FLIBe6yBZ7s6/U*R=68<zUmcO*jb,lwYz!&O#p0|3O3+f0VqN|zZ$|ZT2I>OnahEusZz+%VYF2woq8<TSS!B|kfDln|*2ljzB7F!7XexC;<yrKMy,3ldl68C'gszD;'BwxlEw>oEm*s9*Bn'mgN=l,>Y@I80WzP9v9NXAZho-/3ZwwBXyf2pQ&WX/eWXCMyRi&$+tdXND!Vldt3|;%SG&$,9BbJCxTsk/i;wT;JBnBA2;9X*9N#!eOFnIE2%4T=!mt3ypDvm1jq7xne#$gfj!N$QbUU#2M,MxN#;tQgenlC%bi<K,ZU*>QWX5Z6h-UDi0IgQP$I3@540C4h8$|+LFDxwIH2X,j9%O$mgLcrMGeV;H'L9+yQjB9/jl-z'IlI<oa$@Rg@FXZmqMQAf2e#XIlpHmi2JX0'7o8/FfI2<O6H!!-x//$YMIb'E6$t,;OjRSnUf%cpM3nscIv=aU>Jp&V+Wg7SIB60p2DlSkRn+#6b$|7mMWe-7Qa$|+MIHMoeVFf138r+-X4jDzWs$wC-!UkL%MJN53GpN!REKuX!F>4>Em$LM4@QRFfgw<f%w/W73S'$/!Z2$W2=aH,B8kgdlg7@uWb8B'm7HtwdE6TnGrcA2>ucG&Ra!68oSj7MZ@pVHNUbcR221QkmHfiognRDaw/47S=$W9W6gPGSfwLiXw%9JR9&0JNFqKi52#h4i<9+vUw8Nlcry/zWKpWG0Ng/7NBgZdfIuZx,G|YU%y5kB-EjmtTvG,D2BzcNNyQStHPT<1Qx-WU3XFye'NE@8!yz6|L%Er0XVG!SjK=Wuk/u&<KjUz>5$VIV4zesJ7,#vJD9M!K9bw!JLFOaFFozGElCN4j/<c8r8Z/|Gq*lGj$FQVw40;2o85@dm9IxgfU9Li8Z7eVpzhWlYz/$jX1Ff/POSmmug|qFRQN+q4u9lU$Eamp&D,5McC;+T'Vzl@%!3h<#|kw1gETY0H%0EVKF*WV3<S*dL5-S>*WtKXueC-'@1-dnuH13t=PrsNdMj+o>mNQY9Noi1YNvuBcR*IkOvmJWW+e5KbX0zVlx!=BPr2@=f6QVyVQW/yuI%azNv#8u-!Gc3<M;yx99d;M*!L@8/VW$A*0L*O|oQ9//kM>#Js&Oz|eGEio7LECA8t2TjO5rlAowplh1qCF4zqkC7ecvM6/'kf8p/rRN=fD;PpV>Q9N8ASXC171>20u<ck+DeGDl=&NxMqMSHroxfTkxOs!|FB/&r!/S!50zUh-fX&@ajEoe$e!94-;10h&xt7-H1c8T!cI*CRutLm6pvPgj@XKo<0f*MddOypDIg$@wkt;CjiZ2fQ0h<GHe,-rs>Z1#zT*29T!Xt6U5N/uugWbceh-mi;n9ll04iHde8-lG$@7wDR0upbK'5|YGEZo#lJI%o/P4c#K9UGdOE2#JIQ;+!4K<E3q3AyTj6JNL1nGz*X,wr5nkGG7bK*dJSS$KkDE*Pbh6I''EsY4,9eG#PZLYRF!Ut2kwpeb%g/>lCrEDceZ<R,x<='Gyk,v$|X0=Rt5s2F>esoS5jTQX-#*d4+<ok/;chs12EBLwK-#JCfBg4uQSsrI83'tjOFerAhbWUKUF|VEE%s;5w>s|Kg1HxgMZRSIEpKstElCC6#d;@aAOLvol/cWV;RIe**9S96p*UONE1#4fxKQ@n-N&r|Sl51pM+6BiNaYn$z38TD+nTxtO8UJ#iUGIEPUNCTNJW'+BNnJcZIl0D-F,+suEk=EuK/%#mB9XcsOMK3n1lwt3Lq%Ijn4l|Hu7LV&4yS*vJPZS#qiGhK@uxvK,hp&oyA8KzDH=8%*hpKS!xrr4IvsNT<%MGlk7,l&JmnhBj&XHlFpH<zWPOBAJ6b'0-KOi>35Ri$/e0AJDYjy+>zw5%2'Cj>$$DJCNE&f3n;'+uGtsp&;wH/q>A8!waWlOrc3R>ckYB$f9zrhDwSHSU@>o<v2XbP;r9fGwASVrl<nt<Ljw25CR4>vQzq+xjZ#&ug=-w1/GB>Ttd#N=y5<L&w7oQkXnT|Hk1J2wjs>#6CIf5276|ofT>b5E||cYiPdf;r2p2&@/ij$mR-5xPPLE4lG'RXD#*jT0uI+GC8/u7UThkI&7whXaFcE%0g<$<>vu--n,WizpdPOsFZJoX=$D7pZ2#Fo%RV+vg>sr>6a5!Wh4b|5JerkR8sm&>yd+J0;-#NmopI5QzAHLlz=8g$wuL#z#LqofCl>oak6$ecHInKJ6Y6$03m@P09QIa2#nU',WFZs1hFv%8I$;<l,H4B+RcTxxn39g/BdGG<v|Px'Y=EvcQxq7<*&bmvWUy<Q!h-n&nn&P$Bx|ltnUynKD=5-@hYA|7%O>GbmH%p'owtHzm8b9RIprGJ=3NarlH@Pss=%l*dlpegrd0%6DTfl2,j3s0cpqH#|/R7hXf'W/+|q4-Oumqc&V#66%M/d#j5TCRaVakW%8BO3g1ySWBOrkLC,/je=gT-N1z6f1!H8iVx-N#uO=-ME=vjMdauBrPKy-63R5M4zGCJWmzH|J#&VOs#QdEW**3+uur4Jebw-zxq3eDPmLuZzxVpOm'i&AzjL&/27p&8/8>3BgSIcBwap148D1d7No8*f6E''@O8hDExY4Vzsh@Z/SEb!06+3o|75'kESoR16>O8a,pZVuH8iii-$xLXKncWTieEpyT9cQU,R!dygGxu=,M$wjJ#1Zp>fr3*AWaLBkq+VF=a;iVzXLNlHnnyqsj@LM@c1UG=#m60Iaado@z>!czYC'sv0$+3p$Zwk60yOxo/C<ztYiBMn9bAhFZNXey8EwK#RzRNQ|&i@vAF6qXylNbF>$Ff,>Qx-%rnI#Q$&!N>3NMD-h-@HNPQsC'P6p!GFpuA3n;m01M3x,P|j6XRZMs$M@B5!o9|WQ7Eqf4O*GsjG1rh6%%6,TX|z3a&1#;,O/%Xd-HjFXkwUQPPovjNiV9#|B;V3&3L@/nKp*Vk05Oq;icW7m1YccY&|@v/mS1<,%nZ-x'!b4002,<w5wx%YBAo-c+Xkvv%2qUE!GW*2#O57+JY0bs7!<31h=rAmeOZFk6Gn;hR4v2mF&&>LU>Z8-,u32X,26wVyhY,OIY4Kk&I>iPRDQ1pqHac83T5p>6'uwO1B=za9/7Zsy99SX0dCK-tlR=0E7A9b3/PIw&vdlvMI23F8Jl,/4ehG&N%5n27+i0pg#$>$rk>Lb;&x7*q,4oM6pO1rC5alXJQpL*y9wZ|!6j85fZkx09J@Pwe%G!lBy;ShF|CQNQZp59cH1vygxse,Lk!CL148a$CMW%uiLAhT0N<QopW1UA,QSt$Q1GTL&'xqG=iLosQa62PZvZ&x#q2qsnbV0F;Y/@|jAYusvrAEs!h1plPC&%dI1fW>x'dX6&h,BiUZ0>qk$GD#Pz%Ba5>Kd>q,2'=gIHttu$9FEWSz4K<Z=YE7'!3rSnMcSXX'ayfkVYh58p95#RGStIL%ntu=RWR2|8-<3B5hYjwHR,CzIh1YBgFNiR0M;G4yMb$PXW+=z3xqXqDs'!Gsnos>B#|%Hk<HAI26DC/K6'ufL+x's8VY&kp-b',D2U6l3CL=kV#d9Axp'U9Fu*eP3Yg-c*G9|TFrbT9U0!RUgUa-j;bO-9X7gj|*o4X19LaQ'MuYK&g6SN0Uwr32dI|lgwT&%0o+6Z/lG8y1SQW+K@gno;yo'Nyt!CO8rFca14kamzM%2H&TQC/YySQqGSEwfF-i-y8YPS+@%w&l6V<#4lw$|Ow;|<x@RmXhB|O-UK@%V8-57oJ/,K&g=oAY>A@8crG;hX&fd!/UA;T%Q2kN+ZuZvT9m|1@c8t<JcT'@>E%ZH12Sypg/GqtpzWp65KS%%HP73P//,c%qrLtn3mR>9WX7+;+Es9i2%p!S-rq9IpIp<Psh*0+R9*AR!U%0jrt8Zx$u;KW2224'dm7-HtNSe6no*Bng@EK*,j>0rb|F4RbkMYcORf&y<@sYr6Pgz=>KMI5oWGytgsEs5ea!%-RYUQz-+k>yWuIj+y'G+'t&LFk<MHnKfz-v*NQE@/4t3R#63UG@9h5'6&5&;qnw'7apP0ECoNlw%Fj2T37esepX<880fC=j=%XGW8Z&lBVbN7'UNbAT4AI&ht&$!S0upRyL++R,4f68VsjHROk;!A%Y5ZNEt8us3efx=%Wv|J%Fp,WMs9&u@Dq!;!DV$*RkQIJHk@<H>>WKnT404$Qb<dd=B@$97Vx@/>xfLCy7-gtX1M>n2ne@AZAC>hsWG&mde,ZQ1jDXu$<8#i@6WR4Bouu,IH8e/Nibmi3Wq33349sTJplO'uROg/E6k-A3yg*D4V4CXYJ1uV%+%eG/4/O@3E40Iyk,DGgcK-qs1H!u%!n4kkojX@xuc8KX76Vs+ij#o>tAad2@jN3|*iUYlF*0MrN75Yc%9NW,!ogo+shkw/&sCu-CoV1Pp0<+BYl8zMze*<f$qSkZG@vuF2d/Ucxe4!asKKLhGwb=OM!Z&FF#Lv'F5d$fp3WkYK7t0pKwZb9KcyaoBptrgA!xhsS2<,z3Coq2gdC<,Y-m0%=h<#BYhbs>gFx6l5;A9z=2CDkQKqsp|MW#t=W-XXlQwL4yQFHUM8Y9qiaf+x4W!J/Ruf-0Z6UuxRcs|8,sVD0BWm@gd$RDxC@,T3AQzr*cxM1cLZ;b+tyEkPHV5/B7'jny8|,oEkHt7lPRrmqpgKr2u8R'@EfnPm6B%JZ6R8x/gC2XoY7ssuLD0&i$bI2&Ws5gFCrrZp4w*K$7jW#b!zJo45gF/@Ol/L99g3F78peTmbP=oMvfD3DIMk<UEM$JHG'Ib'UO0J|rvY<cNNlg!BpbT8oGI!W!pmnsojNUR2UJrJUf%vFxD8v-K#lMDJ1Y'Q=l4#1Rd-Q@N5inbvm%fTN3R8urT0iBsS;K1lRXWB5&wZWh#J8*kI1v9=,Y1VC<vtsF2EPqOAtmFS8ZW2xO|x%Pfp98yeX>wojq,|QZ9&,6&yoU0RM1HqG+,1p>Yp%|0P@D%5L9;Bi>==DSRyhoaHji,6XWEDZ$*IlSYrYu!m<79krGavTbShxj@+y>'!qGg5ao!kWNFcSlPIO5+J+O5dd2Q4e'D'5xh6Yde4V4K8fzKCKN+%1r!H$*+fLnAoT;,ST60oJk0qw9$D/,DqmdzhrGYmt%M9<3m0&i8qP!=LA5Fa/xfF#J%1NwTi'mV-u8/L*R$Gabw6<AWAaRXRYafDZ7wX,c8<&+@jndVun=jw|O8#+h+MprwBX|$iXPoD2A4DBjE8|#VD=IhCW@khNcIQQenuOcn1HO5=s'GaXL+S-as@;>*pG'DQ4AdKjh1GvY#v=HW>oOUZjn0n>E'1-F6q#ySXKP!mYY1t7>%HP0W9IEf1NAml;xvx5dmm@7#eE0<95Qhm&6+M2nr0Q'3e,4fWs4X6z'#PiKy,X$%9;S!sp&%OLt'J|=OjmWZF*oeVAhcQ9KrIx9frNoL+B0g'Tio6M#0Mm9$;L-s9jK#xtNTOLR4mwdH@cFCq25DQcOr=O6zRHrP'sVxWL#7j3GWeZ#C06O6H=6ce+Q24rF0J&Q',X8j9B@R#kyDXuse;xgm6igdKE+8YkL*;rXBU+7*pLtU;GfW0EQ#CbS4|cQ'H3i7|8f9F3Q3PL-@OOkjP4$*LE&Gw|I5-z%f@m3#P!9Uj#hzLST7MBuTEt=2%Q8|Tch|p*|r<sTG&wfo%m'uv'pl3bwy30wHxl7BTsq*6srn*q-#LCe#aO*A>U$%YnN>;okF3cN/FXoE/me/CqWTbL&BIIXJ7H,yLdmLjLPXC;,S6sSf3VM>!mBZ/kxfc'iy63ARop%7Lw3HVMK6K|INiT,V%lh%Z7%L>zR&JtJk$BEAtsAnZ=qpxL*C|BzGAk&eM,2Duu&&|*%2;9#krs*D2m/JOq4--tc,gO>2@mjMFT@X*Rw#G0MPpA+zlgJgSdA+5RuhYT1sYvhghCuA0mih0wo88ZtX+xa>X9>Vby@lvUpg'HI*x$e>EpXjj42Yf2I+|@Lk76IpUGtjX6#,h;nF1us5e9MRmps@ruqj2&&mcmys!/oxUy@oub/Uh<wj8RptGZq!L3;zhG$Ab/hnSwmLxGn6F6mSou3$=l%A#'SJE8uAdZz88w$mz$6Bw6GtjOnUZz6C/WMg4!'e>uZ=aHfu1#BfgJR2F498xTAQZG$QSZS|gHp%CQJMWP+R*|uoknVoz|lwnf5P=;U3J4hHLmLi'k*SJLd<hZBbmSO0PJ3TnjEIEeas5=ZhnW<R'&hfc<0'KuEpk1e+rXh4Jt#gM9y@7fOET-hr@kE|Xd7$qxL|uwa0Sh'vuQVDdPS3<6f;zI8TFOOrUAA*=B=K0/858Z*1$aJ=tWW6oZgsLPv'Xqr6+4alU5H=5<hcAvfhDW#|S0a+z0xBIpK2'Fh=qszjxTh*+I>wQhXG*<!c65I<wOSvlasbts9X*sCgFqXu6z/3>aH$fBsv7En3NeB+o'ul1kSEbWw/QwnmQlr+o+/Rk$r2cIY-V>XV9Zl=-Ho*xA5fGUzjeQg+#V%e7e05<rnyjr1<b>CS%L&yFm6XiptHiHcI1k/bqS<%HDzSI9Zp2tX*-#$nrPGPj<9R|JCwiHpuI%MJ,O4=lPC56SGVPQhPWnfo6OU>>SNo5<HxR6tB&&eK9G$l>/1TaEvolDary7=|4A26yi!0OlACUVU;%Qk6-y5y|A>*$;1Lao1LuNF-<vu-LYMjZ36Dkf<gFKO88b3KLD;bUnDAYof*rfO=tUYdwyh6O9MOc21r22ZyZ7U@03M7/S#BGW8G3HF,y&GCdXV|JYR/Br=>!&rn$BD/5qN*7@Y*1uZe#J*mMFhNv66vSPfiN%'x@5j=JluWF2ch#&<**8<qaTgUoJk6w|U2i+7lU!39z*Okj;zHByL=D@Hpu@QURCV=QA&FRqAD4tBc5Jh7;a@jX76d4pOpUR/9I'MpP+e>voQK9-h63BJ0RZwcO=cEl#0KuyQ$LI@iY!&V%I'jZ1O#LF|ojme=*P2f=/KwA/PaqVE*E&/g*P/upInCpInh2#D/CG@AfNqn8LW&;@!mO3#tKdd*mZkxuo*LJpuHNuMZwn%E5;%zJtoZQGkxww1Jr/LP4Ql%$#uKVgZ*Ldp|Q0SclI9+HT%BoT@0rX1Kii#sf;fzj+Iv8Af*!osrL=gqUPZ++v21R%=Vaub26WYTX<uU7-r;v2WmK=xP2|0U3MIGUulX-wnpnBF&>LL<ziMeN;$Yofk#uBDXcg3a@%&MHeg8XY%4cfEmFP=9g$hxP23OahCOA*AJhRUQwVvaWf=KH&nhwcKusbt=rvag19aFdti3xR5rgqa50ltR1<1G,PYSspM74mm%dnqOldue2bU+G2/iCq0bneW3zaZo0y3flogHA0l4OXC@7N;Hf/J7U58Dz-qXG&rGY>'4Xw>,pyt,C@/IlrGh|y5ERHRBb*5YmGLl<P7sVbm191GAaEyp@D#;GgM'=|HT+qvq+Hrz#BEK&Wwtdtt9NMiv3&3b,L=vJ$$eRUub4fGhYFHeO/RG#|y$X+4*GR<y5-iN7d+lcUJucnN+%/70wc67Ot36uoa+gpo0F7%emr@%5NvHgQ/fg&;@15bf<b#lJZ<oTV,i#SOX1QH#Dm6IgTsoB|0u6|cXt0fViNF4Eh|Vl!ZJR;@eIXA0QZOja@=EMBHJyLF+ooX*Hvza>7M=IkGXwiujpi>BI*anekl0Z5!h>dODPAb1B>AR/'qG=G!$BQuh0jB3e|XF=D7F7T8z7gQPpK%h0&Zuf3FfHBK!jnCsO,moQR*R*aH0J%Ts4HA!cSycMyxyTHM9n+jm;+ukjLs4ADo@|a|$u#UA%J@/$7%l@K0Y+rf4q1gI0%Uq;e'4vAsRO@ywWfHjwAi;8FMVAXPE'%e6%sOtVpvij-to#+H*;m/7'z3>|1R1#>Kh7XF5LZs/uFB*s0@P6;qLmP<+rkl<f7H/5v1qC6aA1$iU$Fh/<X8&|Gl,VP8PhpYWP=T$ycv6NNg;VO31QAxTA!GybzCBKtUHE$3T<B*AN|%oeCl/OCFP@f5KXyQS!H!i4q9q6yvlk=N0u<rrozarMmN<dE|1omvwvd+;g2hBDEx<uPogB1uGBO=u3H6;ntVHs<WQMMpag=J86Ghz*8rj&qQ-jL,iW95hHuGIi=k'@E3%r$o=X3>tHvyI%obtQ4hx+aGLWE9hiFLDZb/<XNrdV6Q%H#CAQuR5qWY,T/+L,wv0@6iBE8c7aO,sWeO4c%wmP|80lWqIO4PcnQjHi-Gls4s-gISkF6y5f8Ji/C+GHswXr9uq6fvND@T@cIy*81E||ntGs93gZ#S5d-vvGb@@0ilO%+=KVBvxz5czCgGbb85yw79koC<v%R|NW@=hJlR4,Fn&LgE$jKAr5k4c,=cO7;pzIq6p<#1<f+*n'DU/gwmpMwF9mpS8jerJ6zxcf0o8Ur#c45QdVl6/mhjm@sIKB2&4+-Q,|3U3wmG!fWHX#lrLOah|PRmyGnl8BwXco,4lO0RIobae$2Q3,4u++BDi;bhZNzXPMP,|$P*Dm3,hrC|I*HiE|waP6NB>$x7J=1Lm;z@Sfrf#+PMZ4G|#eit,@6Q2QU,M;%U@t%vvK/TlwqnPaW7*/KjmsN&2sN$!yYGYVeHn+@gOVeDzdgowC1<&Gc;!>--Gq&=Z@tNjG-!&5ZBOi@M&<1ZPgnp'*wA&R6px-dxMRMdeG5eUGcDVC4tt'#'kz>$bJZl'H4D83c>&qm;*$GYQ<L<APXi;-pw3>hCPSNbhvlSko9Ikxh2b$3Q$%fH%W#50ta98d0lcpShNS6mtYD62+XXX5Ek;8v7Engt'EkVi2qVzbS,6g'3Cby-xPnh#=>Nws$@Y&I-p9dc<XnGt=HbkE-tA9IrU=S4nUEKp5>0ZBQ$,eZUNoO%&d-dAn&zm3BOVkboX,1|oMiV>9Ut*AICLa<<pJ$D-f6|iQ5Gh2D%jF%Ou4L/nB96T>@CAxCd6llT&B4l$Ti$cJsiNvh=ofc**I'/|mo/FF!'l@gVPq0b1/'nPC=Y!qO*;k||&&!L*x8D>!ilDNI-$c5Qz$rw7T<J+en61eiNvAdSGX>IjD*ek@U,prqkQa,>paKDxmY7qGOBi|32xg,|$|RX*!e=/iQ/b4i'lVFw!RnR7V2>AGv93<5dj@o!|skG,6kaHB2e%G6A'K1stKPIoyPHDS3X-CR5CZ'a0blld&mR+Tvs6neo/pay7&oO=UeD*M#qS;x7fv'1D,@'ZfNxDRIvVLf@<oR6yyXAxTL6I9OqxHJIOw7uKhlFFh,C'nqknL'+|x+Gpi/j3@FCoTO*1=$w&UW;j2BNe7Ej1aGaAH;NpsvhM0obI1tyC2Y@&acUGqvdrUeI4dZ789OLDXbqKj$pi2kmTI47SpabMmM5kRM$1SE|3f!mgOkajLZVL&h|%!'*Ck|CKbIuX3onzxGg%Cy<|tTM8g2c+Zr!@b4<kOIpPaIlI$Bv=N4-Kgc2AcPZO7-H;nZ+QiuPvP'|u'Y&t*17#FS'bxEK*'H+jMxpxqqCqDg#<K4PBJDy5ri7z/R1cY*R1RLsz34Hu&|FKnVdya,1|,ISA48TMx2fk6fD4X0T97MQHn$!+zIYbSnc|K=>Um@yQ8=|XObKTm=Px$i@M9=z+a6+axWhXuQ5&qT*1NCIJ*@RYrBDGkkLHuuC=o8vhAHft%udX1tt>34N=-*+!obd-EKd'FeLNW/'|-kg!YEY<LOu4MHHlPQ8z;=-PIHf8e5V6w-1/-/fjY8D'R>IL'4!oA&7/gUe7%0c0nTm6u<j31Q0CHIlI+,L,l2=FKrHJC2BwQphDwS/6j;XlAf,4YgC/zUaJ-LBzZFGXR&ms1v=0q0|59R54Y=h1;-$W&X$;u|L20vnCxjeSuccp|P;s0XF8DizEQWi!>CGpVAiMspd*+Zj/OZsds*o;$6wjXhh=yA$oLZU#X;Q*/3<UP,g1f*fhweVGBdx+xG|<'-#@gyP'cbnAg2e8Q;w;,Ep;'So<$+M'wklgau1%vk42xsdyqx$4b-EH9'K40Xk*0mwUxts8Z@IJNW6S2E6<gDO&V!4poEYlRCwK<2mp-00Et%&Zx<3qH3EAV2A&%%4KbIRkHP@m44Zstid;m,RDxIIFdLcHW|;aUST,KwFupENjfpHkve&Lq-ddrUcScoPiR#axn=KhXaCr|qep;CSXTz-Ni=K'ao0$wxMk%k|#rzET5a2wLQroaX@V@Inm2G6pYv|y5sHM/MH#gW33Nhm,/K12'%YkDMWtd;6x,VAI%3uWqi3Yj|TAb4&l*$Y<ixZ0oID6+a976kCYnE$yDES6MEHLSn-Q=h<M!Ck1zC!oAYp2E*Y!ljIFlF=LdGYYgSqnytv@kYkGM<jOE+Ao;2o0MFkh*+b<1%oR=-aRhw$Ra7L+%K+=yBNI/B114$|*PUj%QYn<D,$Y6a2A%%e#&08l4m7OdECCZGFjltauwjFQIXH=,H03Lum2<Z2pn%w9v06-Rfl7r85!C@Ug<;Xx4I>O9JC'lPUf9@CbOqF*%gt3nxf4wfUD6Q9NhnTNQ$bE4s0SOFbdkHzUNu/WfA3x-yZzKI4yD/oCYLreA<R;GoGd,g=1!OIKM7*@kvT$WWy;Ql*s'<<tIi>,C>j%e#r6MdF3<OCAq=pnGbht2Mk4Bj-SHrfOXR9oDlc$,QWWBG$+*H>UUJT+b0puGucg%N2!;V%0Tzu|DVyY5>1iqb9%7Ik*2Wg#S!*tNWpW%l$ZU;U1lx49SOI%wPDgzWTEiE5'sT;W+Ip$a5HrJZpsdSt&EnE|aCyzJmlOiJ9hGo&VQPX9-;xhW/Dfilhg-e4ys|!'yR6U!pc$b%@#oP43CBBjY//7dtPsXMe5KZ1oJNwz6/L'siNiSEYL;yR6*ns4hUem%*dqxT#egR'LVrM3Xcl4tE>9|;WFB2KTD/NcH8uieOA0IWQYJ<OstJt8+7'|LfX<=HOygkp-ZOmJXnWbysiK@k5iVSq1@Hw7rN*aHs7bOZmeE=gUYb8-<OkU7VzuoM!M<TUas+%'3e;H75ry#Q/qCdhx!-o=iRMGKuQNB>ix@fa%yU;VP<qr7+7JpRjOn8|=nszSpgt|j9V7GoR&4n9V4PihI='3Lmx5z8,1=sBA!&QTYWGggCb1!F<wXbfUp<STF'QtCgWitk97k3iYzj!&/,CumOqmFodGw-YUTC=0HW#jJf7<Sf1kZCi<&HzhFmKgx7m6Z,ti+EV*t@Qk@3*tk>V$$reRG@R5Rt&O/w7G6aV*x,EootY+-LiJ1-hToOLmlcl=UnQ6G2KxLUoJ3-GE/EBk,jv#5y;qyx'=gHv#>arN@y+>aEgzs|hktFwL-3u!<G2gW>Kv#lQe&7lt<06sx@bo0U4p>YG+q@,7m@b|3uMxi6&/i!P;JxNVGw5Wn5G*@;d%A#@|Iw-A,/SVZG-lwG'p27Pat'hMdbw5ubLDen=#G1HwDMuZEW8/GZ2E|Hg;562!F5x4pAWR7MR,UrvpmF,z9ly3sfOVo61Vb<0r2-75IKAi;O9xKxYYSq$07RM2Jd4dCb$wWt/+ilVt+Z1f#fHFX'Yx@>cF<=#p,Tak6,ZAoDhEs5|,'9qUkT#,d'ch%X*PozC'k>dEAUXk<8%nmlHVlM'&MDxqsZsyOdP=$*NqvU1j2'c4P#8Nhka8!bOn</%HS!Gu4AgN8Q/'N+c+T7jOHa$LWYoG->uoVmY'r1#1$Y8%xKc2tlXgr+'7>kJDU/kQRQz-geJ'cIp2!CEz10IBGB+T-Wh=#BzqS>8<nKJ+XG;ttKRgRZB*/Q/39Gj-a8oZn9VsCM+5jH%/nIguGC5Crx>hVyE$K&HHii/>x'#zBCPoM,Oh7=p67Azb$C2@3JXzM5DQF4+Q!lFg'CknnhPOKwg3qZdn!kVa#z<3u/7J1vJHqF<kxq0QKG4-k5Eq=!1vkcnGnQ+5&ET2En6s;zQu,'fIL5BN8*/2tl8o2ZVGanenvmd&LzX9'3#A0I6qvGYa356FJlj*Vrv8RkkuM9<HYZCYW44srVK/*3Pqj2fa/yJj#=7;tpxTlIr@HE=72Iki+W4XWFcK>VD&/rs,UrY-'k#Ru>p'v5aKyt%&kQu@>fM&wNjx,#MEa+p!-<41GegL'#jO2e/PBygtcFkShPUZRhGV@gj,SnZ,#C-v%W'rIMF2dvk4S<gQ'@Bsa$@IKz+M,IFFR%'*,kQ3gr3LqND4gekOdu;!>Zb;&!08VQaj#U1bAlR47j@XIeU77eUi6cb/kP/Z-FxgEy,8f*@;f3l4w%L*&P9yq/IDL!>geCStqVDs'XP8jeBeivu8wciKOB$XyXnXa,1&'u6@qFFtOND#|sQMQchb;I&9#WyfW9daEgO0U0FgtoawE1z+<nS5Rj%%9A=R=kTGE>vDmL2S4;!rZ&+1|!+>Rm5>&5ql4#<4Ee6sLmn=2Xw8VBRBfOv#GgwMe4DL|YPJK%0VQG1b#Ub5=uSzXH|oeq'Z*rx0+wFONY7x1Luhs90OenFfD*f-a&POw9d,,3Gwj7o%eeqHnNGg=5/O!hUVu%jX5Mb6,c-Ng!C7w$$4R9$mAar|7POn*nr9y+G6GWTcR@wt*wNH+2m&q|JS-hTvvp@DvjJ%XNb169>nP40Ert'IARh7!fHZECW>KEJV;A-I*1jh0lg>0'<oR#ZNzd/Kwd6S8s2k5zwek#8eXIEf|GbgKf+ZSUd@ii<>8wznGhonx|BC40A=-S/B|931BT=nn2Hg1'q$'h2l<;b&RYpUl$h*/9KvNPan-LwMK<MHnd/HfqI/VfaW@w<N/O/eC$CsyhPDpKqVF7PX@%r0R1!pv#Vmo>sng-8QAlE!22|NQy5##XXo>n0yk<|OzAb+0!,&9KPw&qyk9U5<rCSe,gQE-hj,O%nw+WU8t680pZfG=BYXkn5g89pl2=-4sPTd18UX&nta6pny6gV+P%x#OmK<BBLT!z|#6AY2/9H#iJl22bsa#6=<F7gs$k1%y5$n/Lsc0,JU2a$DMA84BRI4md225Kdu;|DYSRGK&O>i,T4Z'|!P<T3W<ExEqrDA3inlQ;7'uZ;!ZS/UAh0<zmlI*lV=0xe*Ea+dV-9I/E;bJz1tDd7e%ugtqkYLYVYdCrfOlA3RNEKV$PhfRLj#-aun+rq|w*94mRMWHmir;5JIS,|O6D40lK0ELW,DO6gjAg-<4'N8MU%qQ;@gnW/A|'/|vej0|mQnK$!Vq+XrRFbZxH@U$ZQAyU3pTdUwgw0i1S-*D+7JZ&tNuFk=Wrqd1w8YXYkbl0t%Ol&|F-M'+j|bg'yBr>rExoj*ex/g8rw#4V@S+Q>;VOUYQQ/eC3oKnsVI+lG1WRpJXoO&Bm6m,0inykQ8$SX#E3DXNMQ7L=I@l%cP'c7xJO@h-fYURz-,o3+G!bBPRin2H1,'ghS>DZ-Px9e*Lm-4hOwrTM,9Yr<yf$$cd/iakMIa<o>w|I|lp;&bpyxi|lsTAcK%Naq9N;bqgu8v|xAhBVkJtQ!08LG49b*xSios/#TMK1/UBaWgp-A'S2@/==bKisl'N+r8u4'Vc>CNewR4psBciJOyc*;%s#sVHy754<ZtXNm>4O=6bmIL210avEEW@$tS>yl/C+rouwYNMtq-/BEnX4!L!9%jXUGQ0ROvN!#T=Lfr/KcZrjZ5vxO/pPCE8py!$YM5E;Bh+nzp;UQQ8U9cSKnc!7kfKP+p0CrsfB,E1Buc&gCUUmx|VVgq#R$0y+/Uo%RnEG|=3v%YxP2FS2<UZ+e07$@5y|I@-*0*ju#&KRnrWGSRW*pPp#UPbfOgdWEK@3&n#Qpj8r7rf+PR+-r|bC6t2>9eDMX6a8$3XxD=m8d#+J-Bxz+SM|/C6@4MlwwvnPbRTP%|S7tIbW7*lCeT+*q$2vyjy#%qITX/RrzkKJzwYukJoe|oG1jSG*OYL8u7wsmohX-gygO=N,aMNe7C5zuvs,XwlrQJJ7cbaK&DpPmUfqbfxjIrB>DdE>NH<HR$=tOzUb;6MF2%wdc8OsWsCT39ZD4=A3hOi9ZBEEbsN!pa|/HtY1=<>3G8OFHwMyMoTChKyH3cQld/$%18$2FmHwTvmHs5*e@<L5UH9=9nDS=$G;WqM=L%*n'lu,6u>T!LFOF9<ci2X!-HogJgT,WY=ah44lGmf+DEvkv<|HMQ,,4hV&01p<F1bJ,x*oHMPBoHYz',#jNjnVkfl1vQ||Y6O6Dzxp!WH#|;a!Hwj%,DGL75Ap-$99Z/uu6iU<yp>9'A<e+UNFKPybV4YzIb0s,l&FfNy8gQc3a!>3u<N,1y*!!$>8jl9y=@5+dH02cnhAdcR@'UpNeM3@nJ!0eNnsexg0FE+ivSsYtn!KPd,morS&y<7Qq6YwfIilE9y06Eh#e+yP$EgPXHy1l3O'@9'kzh/tzc%Btw@WiOI4qZFG*G$46#SugPImOlm7<wj1N$W'MdaOm@!cvTsMoIhz=IEwnK'4aw$*%>/GWrg0dGpZDnqR$Kd8SE,KbryOQC8+|MB9B#QkV$%YqB|>Ls#U3on8RqBfi!<NSc&2K>r#v,z4-idnH0WlITw&D>I<'eIP2-m!Qm3'5OGr2r8na>Ps2;XtfMBh3LMkHf=k*p*+@;HGewDKrLZWBvl|BUN#4bH21e=Fci,C>7Z$X|G&B2l6KZ=&SRyQYT>|6Kd!tTDEU$t8>EQ&&Ye;g&Tj5e6<Y+!9B*1yUZ'-w1U'kAGB2P';@CWqX9+h|Z88Mbc7PVV'|OV3b3sZiZ#LM@72,IsK*|bqdC+Bl!,bO/A#=f3UNcEg06m,-Lz3;JbSWgW1='iBQ;dCDVAmWRCq,y77I@%MMsI*cdnGarFAag#O2',%,3r1$BH>5NON+DYUlIn5+r1<LGsfwLGE3%<hOJq/hg*$VEvZM,%;q0FBlx%bF2T$q|W;CfTr+@WOVJ3<O5clubHofyimc!PU4H1pFo>Fy0#|6c<l$K/>F=Hb6V<Kan|GbM!sPJDu456%xhy>sV$G+mlsqBVXCoauqCr@!pa@G1bb45=/@3eO#CTGLdC8<tq>YAy6vHL6tzE$,o6nJRn&i@lR$a80cZ831vh5WfRs/8<uAYV@ImQjffFMmzhIq5NCk!%W%>=1f;c%jXrqv-diG*>NxItea@-xAgRH<y25$HzGw2;xwecTm2TFEV%x-XHHKJVTmZ/TM##zpY$wZB@e2obgrI/pojsT2DW%/++WxT$T1k>/f7rc8hcH'|4AXV0r*q7nR1lY1y&-u9x@9|e=/6VW$>xNfr'*E<bEeeHJJoC2d1/TjirxMf<*A2a=4DkRhBo<k'SA66Fn'yJ;|3&KGSt5tKB#bCV5<mftLxebJqT>FVId/g;Nh+KI'fN=33n!yxo<8psyi8vGDM|OV=NbQS94De|J8vZ|m/F39sMQtb!Y9c*3pA4u2B'mW!|qR86AZE=lb/i/rf0eo3>+f@,/L=nVVb'2dTAQj;X<iBa<WZvH%UA1lV1+V<KKSf*dy8=&5Kc9ge6'8$7<6>12hwfzf+k,L-o+yBk9kG|'vEalupwLgMhYr!O'C'3OmgDytp9uX6%NiS=H5a!E'YtsVu!tuG>t+JOj;5LLbn3F8@>SFvDXGKdFO*$YU0qMeJ+rO7Tq2*zBfHLCmLeyV-t=Dz7KEV1a'4*uK>MUDyoMd!pzg8<WOLftip=kW;PAwKiF'nG6=fEiZ-->/U4994Byqj5V3I'JnL=nxA!PJ'8pmtF#rRvdahBcZrtkjyLO-3|PCJE0cGj$>V#qeAdjUcfl9e5Lo1YaGD3UAvXlMvooO|G1dgAPNP04eQ=gzQXv&bkYQ+N22,woTS77tU$AQ&dpIRbTao>lWA,/q#Fxc9aVr#S&hByarz>/a|nvFeTRk,'5q20N5$DOIsty8S90p>Qo>'%JS9>h&9o8UA99741tmyAywcXXs3Qa!k0Y0xl8Db&3Q|fay8@Lh2FUp/i/IS-w,v1$hvXDRVeeFhKAT/c;n0tPEBRA*1jMsuF<LNbo6G37*i$fBgDd<D1+%Id=HmcJqnaHqz#KXBHQSJQIyos1J=NCU=UXMvTy0VUw0WiGfu&=Q9-T<o4*L@lfL>bJN'2O$R,C4JTUd8xvRP0kXMHT2zjiy2r#y,$rtF*oub9h6gE=,e9NXPswy+/Rh'sUV4Hygm8+CC*<*zE4P=v=VAtXA>VR,Gkp6|+/X2=J&Wzb#xFgUTQyr9NI0@ezI<ldoo#rRc'm-NXvn;u&im4+uvWavkRiz>+tz96Dx|jlu'*ABI&IzE8f&McOCFGgj7VJshGimO6MId03=S3qyRgVowmR-#T$!8iJdK,,qNYhDghZ0'f0,wX,rllp+t*K!Sm!%zHQro-GpxI-Ru@rf$!X%oKb>AM'35g/%WQAeDopfxKXh@3Iln'!7<5bdrvs!BHN3HGF-E!|5z9TELuD*!Mm#Gbu&p3Lkd=9$yNyUPAkfVLTGpE#|n9*uzNJZQRBlGlH2MGwKi9@+%;VVny1a#z2MDQjj!%TtKaqpV8/e/7B/bYI8y8B/W$oaVd8$xnvE3+Z8DQITdEPh0etZU%f>7BguWzNsiG1pjVzhh!'F*4TEy#-V0EyK,5|l>-$|ACfGtytmn@AoRL=X2jsAUc/duW5C-A;x*T%$aW+%-Sbpn&Ks|ihH|Dh6L4EhDhO,;5h+lZR|'JnuEHrCc0Jq9BASxPFV4>FNvGK<sa=+h'E+aG,UQ1@J-jsSsVpF/3ZBNzsb86+E*jWqXlOWcD*-U6;3OvyUs*ZC@md##YyBOs#RvGU*K>oj9p8WP+V+r$1m<jjw3;WV1dha6UxklD&<S%*KREmNwo*,;#ZW#qYsY6|a/!FT@Z4jAp*tzxFwh-puNNtb$-iSv;c-nsg<E@i+4F66Z@ZQh*IIyAeb2GB9sJD=*S2i4UVz<2ylWOg-</1=XpqMsWe8z,6lAT9|1vD/9'*H1YiB/*2CS99W#a$yCj,*EVWn@B|Sp=>>lOZ5Sr3*Blr&BQ>Fd&a&9+zSMQ>PeL+d6AtF3D-6>eAHbVSGzHu|>o|Wp>I;S5SiMVWPr%g|bCF>'4uJL#z=l!*D#27kZDBA$m-;@A!<Gm0Z,;YZ&pnfkHC=8-1jE/ZS-bx!&,60UW1IYRb6f%wqj|YrAuV+1*mKzk7hIvJ-6nxOWq+qiG68$<zMo!!nSclK9R8lW8aSS/I*E>&2IUc8,gxHFz-P&zRKXc,J;;69jyq=U3KpcpBfH>2/l|QmS1N286mf'GSHIlpFu7!NBd5@yVg8hBI0b3OS6rYw9vf=oZYC95VP94x5%/Z2Ea%t;tvx24zi@UG#q>|IQ=/hsU|hVE14e3rc0;sTvuBXAd|r<&'rllT0tgx/kW7+86VQv@0'*BUsjli!Or6okd0l2ig;B6|0MAYmF5hs|j,tJBsu%3Bt5xPkTY25C5R6+|JgsAxfTO!5c9m1i;EDWGtkQ'@slI<9;IvAy-A%fdw|2F7fqf5U>AHHIe0irYp-w6G8keHfu2AuKQ@CIt*HyCC<Y,oLe'lG*QuVv725V9Jks/S6;-VZH'hZ4S#Pao,K0/OU*3ai'ukQtZq+52j$5s'/m|nyKmjHGR1o8MW7o<8p>g&AllTDaW$+*Pmp@C32#|&BVuqy9G|luUs6Q$VTby574ys/GWuIUlORJHx*ml-X&8hbI=x8-VAFYU|95wZ#2*fh;!@ap3dv7WsL$jG$D1-5eSXR>as8aY1fb'B&XIE!2kRY%'XfexeGwJ$pdgXT|F+et*d=eLvbG*6-ktnr'Jm%Vr;*9SlUZ0C8zLgq3%TazlPy,<gC!q,YzSs0mR9G52qOEI+mvMpw;frv#a*y%fyI977J1JyrKp|Ay,4urjWV2L6>Jrh|/K@5iWjhEdk<szQXGaxCgw;APDK20Fl2/N=lYJB'mKO*BCtrDCtGRL&&!jrzMNAN7od>7uMi$0ju4nEV3sMi,ScLNqod9u@>Qt*lgNx8Xfh2-MbO<MV#*r4iwR%=@Fk&2;#hjHoyHyL+DSsM|OUzbnV,c!U@P,Aj4Scq*Pz/y9kGf-*-%|pLL%Ll4Y7cMwN8|7W<G=dnhuopTHqOQC1v=SSJ'S||z%y8+'mi3DksC3BTk3,a@wsqQpE|B2oD!q<kH#cAI&rm82p!!4c&j'a%,/2#6X;CP|m=nO1'@9pU6pG0/bhxxw''+H4@z!fEUvH6F%nPMPHhIqB9>|RuYEJl>9LjSVkXCughFDI!9tVf'IJK|,A/<,66I<1IoQvgE*4dXh-lwOf;3Aib&KWi44<5'HYpMMeVP2hWkLSO-<HFzz|eI@wu*CPaCPj@Kp!IcQCIygzVD-JXo3=eoLGFa0J3NKnJS8|L4/AD+2%QTo56PzLSXz$E3V3luTm1|@q'5Af!pOU<gDIYm5=Kxd@Yx;Y4jPYfxC7w8AJF|f1sQ1*pF<hZoC-sPX+N9Kir!SgDnt,oXG'oogJ36QE6X<roHPP*Dif8eMAD2-JB6X4qOSFg5-w56FPnI56+isKbfzYrrKulC0MhR!r5PvyjNDVw@l=3ordlU25sJ63w/KVasuT+W'|09|rHRH>GQp,<xtodMjrks@0Qug!+i2MVR5feU4|a3@<<vpAk;DYe|EhwvAFpn;ggxy=|dSa>,DxJ|kcyRZnh1YIo|<ikW>xDxr;YRgFQfGWa//q1Z$I48q%cJz9LTJW53@7Ihn;BXKB,N<fw+Pam#=N>Nu!RBUG#ISN/BCPFvyLM@5hbdFLKz8e8TTJT!uhwUo83xnJKSl3hV';3U|pJ@Kev9PkPCo>X@EdIbtr*jCrfA03+3g'F3n$fBtm#J4aY+7Fm8NgO5&c2#lqJg+n1dj=TZi,/NWJbuwhh6EJ3f*0WZW5NAINAN'bpMI7EMWL7h0&=fcEoanVsGD%U+;p@XNCR1PotuElQy,I>cYBlW@K=IdO>HvPW2w9WkRH0apwXw8YXTSJAV>CN',DhOj9d10083GujOcS4BB3vzjJ4c'ya4lU$-PRjZ2ZIfkoY0L6Er5XRLgheB|i*mjKldxnk'VSVSk-ZPJ*8ubr5j2@=0<GjNICm*I$izA*S|nuN6@;;xzTk7G'Zpm$8KCVd|zO5>2oBMS4%&0B/#ZScz-aZkd8%4RQ>6v>H1ULVUkRgrg|I>/9v4$VYJ't$YYV'7fb8/i==WWy8QoqfWy89rfEX6nj=eI1RCpcL!CkkDa%cbmVOup1-/yPf-XW;<VHpVehC*3IAnC;z2>jt!-u/VMjacsx!Ar/&g2Zwr1x0vfJlsYG/sibhKRq>2$arQHb!<EFtSXF%j%48YwtcHQQtk-yyzDju2E4MYoHx|G7Ub2'f9&EifApA%k<MBwiH$pg,ZzUjwWOQ93%3fi2MTTk&MCHg1rM7ym;#5yz<>Y5>vPQdr#|Qw'9*YF5HSLcDT6G=4ZlF'nm3#n-6G>r!ql2M222-*eywTkWvVL<GsbWY<Z4B<gWZGne/7@w'Cj*|2-%g0boRtS9KHP6!JiLHl01h9ses&*L76UV5ao28-fzI$P--+p#HMnz@Bl5N<dY1Afa+dw@FB$uhoT0gI/tP@+KREhinN2pXak;W6CECW32EFmfBui/x5GWriGent#KvPk!PQyb1%b8jlRl0EwT-o,Es2$P&x%|MsTg01L7H6Ys6;,e&Py!8%aMIo%aCfbW,Ni|>d5FHq7N|*TiJ>Pnp/rTiS$QT#pKZMM5<#J2agoa;%Mu8LeOn52gorY8DW>f>tsiHl9y-Mdlzb5GQ;9;SHkL@Dj$wiUwq/rgXO,Gnchmz&ef;QjKWHF4CZ4%*FGKxaw#AA3$=e0==9FLO2,72nHpdK$Ufyf%m=';zzjA6'GVH|UaAh=tnyM|YWgAsIxcTQZX<wiL!v/!Md@GDU46qCEc&O5eWi;X&e!F#X%|Q5KguDie>GTePR7!bUF#uz|y1!IKSOZHhbkTsxQJDK7n3G*!e79z,a%+F;U+%TNtPa#yn/XB/eT%ah9dEuZGMTB!pyRU;dxy9Ue|5br4<mrk;kdSlkf4K/7j3n!tJw%ai3@*$5wv>pOg!h0$vG1b3L/xyDFeFHLewwi6h-IV'8o@N+y8,CJ<C7,DtQyEyT$DCl,O,j=Y|fo=2w/p*K1!38MF>7Olbyrn;a1k&>B4oFbY$9jH5ENSV+2;W&/nnSl539%>!YoHi+A6<Fy7FyAQrYeS2X2WX2haO<@K7J4bzWT!oOLzzUuMFx<+AgpjOuR7j/!hTc<0S*rxW7rVk@pMy5gO@%mKfG97375b2zOP*Gq8,g1qewkUtfo9X'DW+Mh'tc8k0K>*-i3*ZBDx6NVenQAA6wEk!,wLwq|eb|@cQy5i4a,V*i2&Knn=Fl6Lxj&C=pjPzXAs51Z&ajwtcIVE8R;=>&-&2zHWB1UgyI%C*G*g'T|ybAotWhoR3&0vz7yqWmkqRI%9>nwTMiq;=31<<bPXxi6PhF<YfA;8YIY0BW;ZflFvLlDdD/b8zMWPH<c4wvdZ761pZa=Hpnp9B9W@A=,ZXvgekA6O9!fxMamhux%N9,#wzpdsEAPZ#aNqUf+b7BDSkwHruMK4-$fz+kp4@AA<t/PAS+@j9zdZ<Q53UZ&VWdjs1@Thtf#yXb<imk=9fz;D&%6vqA/5dm6/3gIAfF9A+oLtl*>nH3taq6yv#UABcnXhr31pAagt;9x!$m=kKEVUQ5m+yL2gFfRwj&yzNhj0yv6=0-Q&9p<z<bZebTaAzJOA*g*IkClB+nw<b|gTLNXoMQP0<9b78JFIll'5eQXfdK@dACjtw@lAiKsNJZ!h%azBFaxXbmu!L'DRU0//YQtr|&ljWRz1UD;/B6MV$|hp+d+e68-s/szF,%y0%uWB@$jJJX8*bGfME%>Vhq0NP!sSadPvDJ0zleBG02ZFI/VWINvwUVhWmj3JXKFYPq21b9Suy5&RQLzwdxR*Gg*;m,ktqfv#jR$qj+6T9M<gV@j4beY55Z'aq<N1hY4G=7JIGaT;g,*Xd%AO,3bYuE,FM=*n=vh6KMIzg=;18X4'txRxpRQvq$NXgVxWBf+jQsZnAOi48-GldHSmFXb3z>5B8lcw8|wM%JsXN2tVwCfJc,i5n4<SwIT>tT*RXwYXcSFtg%!SE<y=;yYTQpC/N%;sMCBzh10dz%A9|j!UUw$i#FnW%3|hZwlDjovAk6i%v06!/g7-J$cbdKA=jb'<8zgzd6gnv%9,DmdG'lCKe0KZq%@tsg6bMv%MbB%i=kEtONBaz4S|Hcvkt4CP5-6Pc3;0>EDo5'$WS/6g3OR,DI#2EpfF6bu%T7w$J$;bPxx2d#;jka5XkBQ@La'4>yw6=&PN&U;+$fbfLG@Xh*'iwrh!fSQ5/,rCLs<'pTeu@CnK6-r*BHu2&Ao@DUr=uGs*M>;WmH0XI|!XvjYQU$-A<'C1x8,kP=ODh=S*T35-YoCX;dz00QmVU=5Kq5nWY0-cn$Q/n2h;s2U$Zx,bMT9%$m,9%GNkHar8ER2'jNJ&h;WT*,RfNa>WCHdWHC0->9oABNwlZHx4N@-d-U>NATn=6Yl%5uThnueL<wYcQ83Ymq5aai$64OwG;xD!8PMEKX!xoi1XhZznOg@3#Hz8-;3O7Hmt0&,!*f1chwzy+sL6@5!cUkAR/cEJl6S$t1C3N6+n&qeaqkL<e'z#yb+L56y5=@KtsLK0uq'fvSR>2b'pXV0VHHcvge7MG>ZUhq!+dtH7N3i!'o'4sXz'RUBKw'Hy#JTcsc$Kfl2PPXiNZ;f5DkcX@hl7-=x56;<zLMs2$gCq8;|hQS-%4hf0n7hlRtv;qbz*o!0C@$T5HEe|S/fJ;2hyy|T7Nyh!v-|'IrvKU6*x$;Az0nxvDjrdS!Tt+lPys*GMd;Zz0JsaTQI'hX8arwv,llQgRz<lshe%n9zTA%oP6XBNs=3k>$Jvwn|Hk5%YSLFE;w*4phrOxqxl=fTV6s!Pu7V*j%8V,UZ,B=!hS,/GDQY2|6r&vHrP&AI>$hl$#OHUInM%#2u+Tp/q*LBWZh&jb+d%fWSr%M|#|26>8,uS/OKR<VKNo;hduE#DkPucwHZ&HX'!jJRtE6t8gCYp&h3l673Q1K16t@NsPh6Oa3#Qky&bmh4hRB46b6XhmDe|Ay>UZj@&v51j@S|8RrcyTFFRQxuK!NqWQsj/32JUpcs+=Yl5tA0lEmYz+B/V%oN&I$+$tPlNfzkbYKZ9GP;=7CXLt0fFQH@Fv%n-PE=Pmx>8pOP7UQ&TC%G'IuO+;iUh!sM;W0XhHl%MoRafoE0jD7C&Fopw/bC>QTxKWnoA-NOMXvK+'zRIu$/pqpP0'!teQDn6V0J6S7W,q|7k5#h<-m$bgI>'r7<r+RyH4<w%H7=LdUfsd2$1DDh&@022iZst8-rAsIgPu/m&zsXst=tsZU%b-A7pI>yt%lF%8M<&fCk8+-gbU2jB#+FS21;eCqI!9|=7wbTtH#d%FKmwX|J;ik;Ykt/FatRi&ZQ9lHpmWM=z>26IU!1e,W|I'%paw7ju|!&WY2&VY<f!@paEd4C@HyPKhIi1QmGUE8uB7xdy=B=o&Xu|F1pPvt2KdfrK6GjyPX!mEC>eHtYf>wy|!PZ@Nh@CwtL4Y**Aa3*A6KovWfaenOnwS&=wJ1!1cvqj0i>B=ajTvv5G%GCqHMgF'$Il9$D#P6mlZBWfMd1r<yC'QC*/GI7Ofb9OD&Y2*Fy44-B,fACLY=wkxd<%6mk+Pbl!PwSFGSj2basi1D|RHvwV0XKzxRAjr!;2r1Ecb1#@KaJQyut=sysh*o0P//,kasMSAR+srM4C*gwg-C1gpGxCXax<763;zKjxaIm*00g,iePFBQz6pc@#cJ>=pt6snBMZB4dUAk0Tti#pYSZPPkpv7%ff<$ed6&&psxpo--BRsVM#mVn'ow<GX;SApJ3Sgkd,$5J#*hC7'dENzCjZcbu0I7e1/sg>n*zyU1SD&xIZ'pEuQv;mEElaVK@i7*dNN7Cj0kmF,=6-/5E1svkq8iAEX2jiePT8J3#j%K%e!!XPpC4o>dFqSfrnhr0M%YqsV4NeFiw/njt5OwH6ZM-bsAm2Hivv>o6A1kO7mN|tlSOi,*s&ooxT>@hjKzpVg-@ajoL;35unZ0vsKuJKHLi@JSZv'Q!G8u>5,C0TB3&XrLNJC#!B4x-kzdZG'SEFGhL$9+AzKc9V'$tN'Cuw'KttjOd#I4ZS38hnnih0TG/US&vER<54Bjb=Qj*N6d3C-KC>cQNSY9BPH*#Fc$qH5PwuNmQXn$GKM*Awg%$&exjeOXaVb1Yk!x7qI5JJL6JrTh1q;6dq-l%e-/vCuF>OscM'siY|HGrEjc'RAq&F3Qpn9cL6/VunYXP5mM!T$<te!Fqr+qiD%K=I64a|EvHQSnH'c<yrNxZWR1jYM7fthB&KJWEZIvS548X>XlBtTVO'tc$C/41@t%BtB0eTWs,r!fbO<Vn4Q;kbWo1Sy|#uNa$'c7w|<f9rc<lozb$Y2mzfQ=DGKaC#l3w+PUy4O5ldyhmK7/xL@r'k<QF$BGOhJXN|z/zsASTF2J-82HfY5tzZp*3SRUVo<btR5i,Tv+|mG$vK74Ix/hRZv9*fb7LDAc3nyApd;xyK1<tGZqw*pH-uxjJ4P4|%mX#2P,Afr0RHAlFSHRVzQM9P4UWlJ7e8Vroa-JVJuf6Q+MUT1nxkdl$,ZTKH%Dj9,OM26cho#X9,Xr&X1yL@L!g=/J7I3#,Nzs*;YGQ8W1<YF0D|X%|ptEn0>BnAN/y+MEa;PKLvAYjbt%!,,,#b,/'*;h60Q<hW4FrP9WKxHkfGJJmLBZ2O>m;*yJiA@'Sx2;7-yA/uhUe/xabj!EEp$5AQO%V,7tUiKZ/QKB,1BLcH6Z5d1C'q02ubzLl>vugidP@r#Wlwl+%U>0Z8qgYFc/z7x<39Z4o!=5@rR|qSCLKmp%8goDEox=5Iypsz!gIjf&<ElwBBVixndU!kC2Lp$w4+ebFBcqEEh&0urwL'&4U1dt5Y+tmGWJ1I=N=P&jpq-p#hnBoSbwV;!mAgDPH/>-E2DZI$20#Es&>0h;bVbAYDK7uSy8ICHeJQ@bnCDgoXDohiE,+pF|-LEuKDXzMP5VeeeB<h9j40!zwE5>a*J&bFa5!=%V,Ur!>f62EohGJ06TxcmvI8/!u1qi,y/V;T%O;bi'=630lFxdkS7fyU7p9cHr!j*!Y>CX@55pOwN8@pMi6MWQg|Z%qcg@>trm0+j#xO>1gM=|7S2-9tAN12INw*dKSV<$>se!2KzYT$$XS8b1S$bXZX1ixtO!2hZJV,|PXPptXoSq7Xi&GLGe91Nho+'61lO98dHeC'T9u&gf!kLV6GkEh>/J7krFgEv,xw$8wK+uiDdu$GjA&nrd<Aa>pF6@&U&1kv32'1ikA*-go+Zjre8ufMRqiJOXm;$MKYc-aDM4C'2Mf$6nL&A/cHbhg*N!,Y%%yG<>@OyyO8ZLXrG33%Y3j4|KP|+x<kU1F*C-w%YYapXCte$ecfHRYa7$pR-1CwtpJ4h#m45Z,+E9H1irlo7H75B9e%7=%keM4gAkA;N!b1G#o;c+r8JaD;F1/9F/CGC'HZp/YMdXu-kpii#CrfoCp*->B8rnf3'V&6>j66WiffzaECzQdm=Ot6HY#=rzuz<vHyqwJW&+P;<5;n#@'DmL0%;S64VB15,#GuhAIU+-x9AdXRR$oMmLc,Jn|p63CHz,CUMB&&<GS/&!'|f2tims+#<ke-fggYf,b9OvTPVhWH#il;;+5Cd8,fTtnfgR;/tyG@8FPV5SAGaEEF'=3n2mHz!ZNSj>bdHzGwn!%E0fX>#qx,4d#N1IzAF73LVp6,d!>>6#cNe<Q3HvrtPBxQdaLv0dUJoT#N%pn6vHo$KK2QNck%dNd7M8T,*dR+8+z;YVk+xzgnB99Z1,cP!CaMh=/FKtXoMlJ4$K|4fLj*>M+otZTQ90MtE7@Y;x$0&fH>DYAt-;&V*#4M5bDrd-ry5$IInd0f2o>#wVxTwNuM9vO,Vn'9Q<cTfCtF=KadQY<lUpAJ#y<9vN@++Fw1'lW|Qk7gvdvBos>e2aYs*zYr$Zplo;hwnHWmkLe8nt-A8O!1=|gIhLfQ7Jx/@W*MR=#GMN5pbN9kk'oR7=#ozhzT3KSRbd1C&OIJ>aedv;<o7,U,AnK5+d$Yg0+lTayKaM6;DS*ETJY$Y>+kmb8ciuHC4>m6Kc%aqP2flI##=yoegq=ya1c>iPLkuV#dFCW&e$6X*@CcqQKxvHHV|Fq>37prnt3B6QYjAzF;azS@jwLd3AK0P3w0|vxw5I'K3FL<7gKK%NocPkVRWhYc|UQURReL94hiez9IbC3;Pg>o=N$kqL3sxM7Sz5|pN5Lpzp<-1sYK6qk!Gwq%2;%uJ<3-uDz5|yFY3M,h>xMz4g<,1Mqq4PBe*qZKSX9#lRA;lWjlZAV0U|+UmzKfxih3r0To%uvYN,>'HTTEXhv75fq'@csnWCSqRRXu7VMMakQw#SgA!GZ;P;TyeKJKJf/|dDkrMx&F00v%Rd=DN3j0w3PqT/lpmUEvId5jMAtExUvK/;+qT14Nn#vm0pD&DS/J'-FK3Pu<Wd@zp<#zcWQ@cMDX|7tO-bmsuc0E@oE/a'it%'P&+bF7TpMtVK9Ed0;eikvog=PQdisMU3JRaDqi'%|4/gpju3upmr06<#+=ppOCl7TIYqvdZ'&wM%#b-Ow-+h-K,;YU2sucVxKcPUc054t5IIGlCOb|<6yh|ULcdl+URN=7>2G5Dlz/*FS+SC=oHP-fIBjAKkx|be1KKByLhV%t3DIC6RZJsDF9lordDgB<$RF'*B$y0gV>IcpdY6NnCU$Fsym&!5&r3Dsw/rwU9$NW4CiDQLiU!3KH9*8od!1beVk/0Q=+n,/@+kT$6$#wN;qT=TSOj55kT,FMQIin83P6qlV3@/>0l1MaF7zhKUr*6tx*KT-lBYP@g0uGu/-ns-|qNeq|7YTfX1q9<V-9EtT>Jjfg$!TJ$w+JS&2>k!1DgRk#0-K+4NkB>FE=MqFqc4!Jk+5T45l&zS/>|4uiwON*L*cnNVejELts3AbhHi*y>n$ELWfQQxlDos0PbYKvb|nu|T9GSfO1tWW4XZK,'$@pKI@k0Ng%=,<ZIprrk;k&h;-|+wBbnoMSGMc9&SpPEAx-SS-YJxr&%;7QPn9!2yHWgxz;y,ELpdfJ+UcO4Dq-bcA-Q@YBEG=sDlx,Oyb>LZHK$V,LJp+*9yt<zLv%c-q7T>M7/Raiq-=ZZXw7waK9LH@a8a4UuuWL&nTAF<OE9%qUvQK-YTBms>aPfOTWCQEm64doKDs'5&lBRIuwrvx;eL9;g28rJ@5TmD!0+<mVNuf#JeLnveglZ$VL4,74h'qLovM0B4ZeN3ODh4CWUHeE'7AvA|*lr2ev@kHwHflOd;T'rk#mo3b9X<|c2++u<fJ4VG!PjR5O6,Q2!XaWDiMUzD$@nj%9!UzbD@d4RpZqVrtGGP,q2Muj#%Pz-WUz2ZFdJ-Q3hd89n*HJ0OF$szR#<384&xiH4hs%-6uc/RQv0,BTNArF0c5=NVzMKu4Grh30zFw7>kINXzx->AD<>F+l57B5nyVLMp!#tNCC+5+IR=mEaPGLw/t=bXxG0dhnIn;RWtrr7M;ef53%&&H>nAgpGLfT6=wFviyO;&u!u|@c#1K=3ER1LKcunoPrwA9G&TX+>u,7D4|EClT|@l2wa9t!2!G*iBdQFq#Lhmu;-Z#E6'MQ-=4uFPOl5JI>I<MBWgemt>lWi8/n6|#|7qSP5gm=Kb2EE<7bGrE&Nox2df$f9-o/#aDU9HnUJGb%Hd5|t%qU|qiM&c@,aGAwi*TgUZRmiyy-bzC@EEs;l3YRY'Q*v*kXPRF$!82|tAxrhRk/#b<wy7lEd=yL%&;GU;x+#vD&veOi5F>%SJ7k<6pDe9nx$9|MdaPO,Vg$mfYaXy&!7cNzm7r'Gd'DekEoael9FP'>zEhL2J0#sHP>fvkm;u#ufslpq=2dATcrA6i@E/!oNX&rg0&9euQyy<g5H#TnV9Mu@rIpcT87#N/$f7m3,Zatt*1YKJJeKHDLeVPF*&mV'F0Kk'3#*0DgWubFzIwB-rxcJmM3aGJFc<77p;b<Y137d<8$,3kaBJ!gB&OC%DAlm4*#L,t=N7p3;r-OBu&S>-1-*UP>QH&E,K,CQ6s@#jek26z|RwzZGTI3oz&&q9h@ZDMP*gwa;jKkRpU;ll#l;>GK!Cmm22>JpU7-EwR!b2Z6!sRI$Ob!I,#n44RFzTss,UnTyMO$ynuA!z,Jxf>A|brtN;b*eN6$nlp88J%cMU=ma!qa=KW2>yto3IMR+h/8+!nUkD;jO7$e9o!y*;WpLSw-NaMp29*Cex6b0FCO-pkCr/#$,+<J0-g9i!HvLE85PARq+,YQ7qckAor3nz6We/y@Wjs7k@TSzplGa1CodRTFSzaIk/E0P1TgLZX2>>Ph/&!6CM8SI/Wu8EWCq1nA,$7g'NK=++p#5gY1Ot3QjID=+AO$V1hj&fVL,XS8JlZHbT'IH0qbfA2ehiO3jejv3MnQMm6!$w9&JpWYSApWS7ZY!V6If2#52FSPTKI%jP#Gcx|e>aqos<HXjXAszPV0+N'D*7W6xk/sQH31>xDYJSmfViO@d6V&kI9,w0G5LKyy7rxGihrpI6b;G%1Hmi72WLiKIO=n&yyU/%q&9iIS=q,-=N'MGs>CfSB/WC$UXkE2gi0,DpT/f4aWRK<Hb9z25%ny*c%jNX2Igdh%j*3ZcCMduD9p7pyDynm;cncLoj<Y%4Sx7sSgQS%xmoC3<es|>vvQes+'T'!FD8jImhNA!I<HyLW5MzlfLbZ|q,ZFeszU#pOO=8y93txZBUs>1s2Q;o9|tda>hg,S0q=1W&;LurrOK!o;nYcs@zQYpAIbxmWorguy;Fju0wy,%SDO=gyz;|YfvBS;bUZ@B8lqU!jNofzgox+JBnQ$e'KqhaH5J7C6q=l#$>dZMmbUFTWRkvOt7tG+!T#YOZ-/6Os'#V2cpnQpyzZ@!#LPE8!I;HeDrW$TKH9=5ess|0%tga@#dqRg$un#;MEWxaxreC+wEsh3I|WtVvRKG0OBFl+b@*%yOBsAU+p+eC*AQ3$*B,TJQOfPFX@-5S7H9mN/pgC/Bmg>Txfs&4$d2V%lyj7l$J0Z6!zCZt&qRw53HX|nF,!C7'V28KJ8jFeh9uKZYb'jgH,=>$g##B!cfEK';7gqiHh6iVDJq8X2kFoHU13R/GC,Lla1bSB8wJVVf8;;UT/x49zIIk#F7l!sQ,cE|F|L#p17T|d>%9y6$*EDb;a6EWT$0p>YSK'%2GhdHBD=pdw&hd->rZ0-Nw#@eX|Eck,m|gxbo&x,V1Q,SuHg<SX+Wka8rYac2O#8!X8M5Ebq>Pnx8I7%FJ,jyW4J5Mp''@RjP=c-kuUDzxKcq|D%LrT$A<*a7$yo&zcPCw,@pd;3X-LW<ixf<N7@<A*qi@p+V=xfh+<I'O$9RVmXEc7O3=Isyw<@B*mIM'5odZ,RU9bo2B2VkIu1J7+-fbVAbB0;$t#b*s&rs6vZO'%XLr9C9Z*6ePDf7J%XDWbm3/JxzgXu6dK$2TERL5%@kZRoWKYr3Xvrm9PFqls+pg6f%fW&,091Z2GN/QFDpf=,fReBc$lTfK5gra'RcV'=n5hn|Jrpc98oppKTmWax@TFa#KzZ*dVbG|JsYQ%9PSY*;=;Mjuz*vj;%Pdm5uQWl!WUUhIVU9v7g$4h@&*m>kw&38KiBVh1m|01X;4jlTq%i'CiT/w!3!S#t3A'o1#,6PPg/#%hTDt;7-=#cc40Z!w;3nc+Fs'$TX9eTuSYw<fWgT3gL!7&B<r4'VyTK-N|6hOLNoYFZDYQb;>ulME<iEFvy'F;85;6W#a=A>Ca,Xyo9mgIEkXuJYGr!N$X'$C$;=kq;4RZ/>Z<k=G3,vdb0<v7%,pFaoeh3W$d-AD*;9$g-V+;bkYb&U20oY7%I2CTstB$nvVG0WOPeCU2M$FSwd;+@EzR<m$p4FPSfjGiNNx$twq%yt*1JD6j+jAh9pK9R+zE*zsNzfVosu>nIi5TvG|yR&8!0saMlYD6TGnQR33nWp<h&W'$EwZ#ynBbA3f<X4de*,m!j7;RXNBFbqv%qCgUuTh;U-raWPPRRGIK@<nz8CcpGLGbKe|9NzRAgM>Z7VdiZi%ax=-U8g|tYzXJD8ua>yfwbSTBt@<*d7BMt|DTGKRaS>o>|z5Tv+7pe43S8c*5lD6'nXCMWzXJ98'hLkLsbs1pO|<mEdUyl!kq58g21ySjc+a|GFn%OZa'k9F#uJ<W<6RRi48O|sNj3>uvs4U<n89Gjdpl%TBRYJZ@ROya1A=e-jKT5'4|OHvRY2J3ttgPE/V<LmjzlB=lzDFy32EQ/40N6CI5E-7wF,mxIWuGPDNgB,=2ICnXj|Bzmahgos&*%#+N87l6sherwvXaHC>mhcBc==yBZQ|O@BxaXDpx6714Lvy|bxyPGw!m45y|1B>K=Z!5PwfxZVpPPV=Qa7p,G+wPzmSdj*IE@U'!e3FQkLkrYjLnhcA2i1MO$Ha67p-QAzF|dcltJl*GHXP0;!tz8g-T+ejT@0ZoLI7BA$%hc>0t,V@5Jzg|9#rynoEC3f%0;0S0<UI;lh#Xstcy|n,JfRFApIKYs>By|9mNf&epplVj1Ad0e=0sD=r4t5&XAjZ4/St/MBr#eNgbVr1wY1rk%vbAh*XGyTmrqj#Y|!H%3cH4d>O9I@GCIonsRu2-ED|BueBz4,4WwKIuCH;+dww%B@f+-O1AevKKBCGgn5A'm4S3h|0$b;oynyiaPDsnCnsR>IL6xvdS;Y>@1oKWvB/E3=|<Hz0/SdeUDva<z=Cw8uZvDYnWSaQvi7@dwZ%/Uc|4r@Sxc10C64$@yMz/@cykiVY,OqFjp*-i2dkNh!FdyR0KIur|8<i65knD2hAIv,bXuCx6O-Zg@'iIw,Ie;S!V3;OT2rVU###&IH1uuAX'-oKH98xixf<@fW4ZM*alDB$=&<F+13Q;->SVo&eiXygb=I*C0&CzILH4c!37@ky4y72j1=S!gpv,cqqa3;XZ<QvSX<Er3+y-o'#Lb%iWya7315by$DU1Sua3>CFIP-RTou4aVX1,C0@<lII/A;+,V!3BKkTI!yVqUtL|=vrl5ssq!i41e9Rer='GQ8T-N5qYkvUu6JplMZO'lerE6P%VP2vw;6eM,$$!ajoFA0B##jQatowR1U#9<5uQnzE$8K'BB'2|f+pl4Vqcg3#2,&s<pD#7psrpu$B1f=h1#;Y!xx|JHllsn,B=OF/-d!Q+O81pmG'&GXYs&QIYO,r,v*vz6hrei2axN@hQjLaOoZ7+14;b3vv*lQ@s0|pSflmG1#O5'5faj|s#RAs'/&'4lcD0SQkIXqk#f20gv!Vyh4lr16V'Q-s2@-tnia$qzqu$Tp&K<l5NLf9IG9v6!x9cny!Z5hmsquqY+%E&v,srLL9fRaN82>OWjUL9|9PbCht/xoC25V520+S*Nbx'z!3FQ6ZrTN/APk66uHKCC;%Izx7=WeYv+*v+bHS<XuR#fV9X<!Gx|mbB<mPx;pj2bB%P=r!#TU7LHSFecCkyXb*2&Gr&&-,q/0j8Lj=B>1bf6'hOWK*INn54WdM/*ua!R$vlH/eP1JYR;JV>lE!U>z'SX#,x;fPrRw7agtBF,6HgHZK'VC3%*n|=dW6@ky7O==O0rlWbMbA6'pN8h%FLx'N/O7E,g&j>&Slh#6uI@Q#+i2caa004DNxcU-c%m-pDmHVauJ7>%Krx=gbC+#R5%Xd@#KhNqST2T2fLFx1ng|<zQ!3FTYTJqPcmC3vOFew3#&!q&m*HHV4t3@vDTk5/CP/i+vxP=+,qk@g@QvNh7SEr0ENTXrpfI%rqav#6XK7g%qOrLl5op<iHxZW&X2dyOHjQC+Li<DH01=Ef>dXo>dAG3c%,Y*W2s+RawsBxu9=lylTGQ|uRY-R183eQMzDPgRaoeBy>'ebWRL<=Jt@VHxrAOEs*b3iRtThgctWqCV=Gbb5rZt=<8c+Bx+j&kEHkO;m19t/8r;1Zi$lqi0!iKY$j0US|2,WMdks!Up,Tsd6CIMeexl#wzl7@xO9ws;20+4fhN$Er-l9tdT@N2=c'$JpMfkOlB%@W@;pN16p#%upvmmXlr7%<F4np7Zp+DCZH0v>$QP9w*a6fLYMQ&'c|b+phSlQKILh;'ft/=ce%7FpM*W4O#fWC-kUz!6iSX/RGfbmTA=p,*XO@G*lVczmAXPWoqDjTAFs@>8i=i=r&J;JLJLk#U=fpNMGz|XIPK54dpd>#*6Am&Xz,DhWCnoVV0iM*ca;doAIfV-,wB5XBT7U435%wdma/c*chqnXZB<;YAiONUWE/zS5/OE64;SVtafyz<*M$;TQ#wK8$4oa|z,igkV1550IfU#MN%untccf1p!FK+GLNQxtFpr43q1YI-ISRO3RTup0VSTIKl42xUD3@ZEUZ;1@MymSFtM+bXD&4KuKH'3yS>rcd'&n$Et2|cR7kwv|xgt+>=U2WLL<J!+jaDRcnHt<'*s18n<G'PRbS5+V=M;@,cTXh=$fBef<a#6x7zLmjfF#8uyJMqMikH2S5=<f$S9pOq@bL1aMzbiyo7P,Rv-I!z>'O*bNdurtpqvtWkwdFcBihdqSw7v$>K%Z5@<iLt8rE|e8=DpCbW72K->-RL0a6NP&2/zLk!Qlm#t3A7>3r+XyNK-RGt4XpnQVyU!FQ'qhwWBT&#|Lw8WN;0-;HIbSbc*$B6KID5pRxm+Q-iKvI0jDJM!Hlpxf$*CvIvI*2YQq5<$&!aY|UTxr-N#HPXxGixhzPT/Dy48p#mlpt-2nK/Rwc=0Eu/,4og9kl/rP>ph@5DOqHb9|IEuXol*3Nch|'Xy*t$3VF0L8F%rzs;DFtub579PJuZlx'vZaRN1xr5eoV$jdjYdoiUbA&9yLqw#wXbj3K<cQh6q@ClL-K|lVJjo=lo0U3u19/g0Y;ZDPL|N0eTm>0h@,qKFpt5Phz%@x40Zc6bVFo09J5=1=7RZ'I&iR>3cWh|y3*2hbm!@INw!NOq3dqfdH90-'x/xgAVCXhhYlfvN'lEH52L2aQ'*MYZ$>QV6%VOvW'eAqFc,Fg9jFf9@4Ys,Hcs>AaRg3de;8BqT=4dX=3FL#,sR>mD4XZO>|44UrPQYKzd+Iautf3wC|SnuYLxFkv2wVJ>@vh6vd;trpT$EK<K;bgA+z8VXirGxP#vtYg|Xz<t|%7pNkS@BAz@E!j|H3KAtWrlObnJn,h|<tmfFLUm1$M'S;8YcIDql9Ez-,&UxwWTz&tVMd+f|Eh0Rca%0ihA=awate1UG'P#oXe>4qxsBw+aDWb7=GKhMO6&!u%XoND>w1l8yheEx&V<,|>ofGUqTbVxky*-<UaJDHV,!j/KRxsj&j4AM8kPehzO|ec9r$Tr*t8e$azK0@AjB$K=oS%oYv%Y;*|@UWu==e;dY;ka|3sjFI-d+!!UP@1a<-$2*1Ka8uJoLy'Ni6-Gw+O0z8wJwNx!$%IC&XvwWLDLspWYAmkQ|2nbdXp5xov/J,PwRtq07MO#u-EzUk+siQv7$>U7ik%f%9hEolhJ$eg*0q5s,&LApo|m$o&buOqE1tT1HkaCZK@,dbItX'rCZ<TBrK@+vtSE+'WQuY=+zl8T9S;bk<D$|4y'NJs#,YfC9yRAAhSotH0|Q/;uTGfTEwZE7%>zLdobJ*FzL$q<*7TEK&6$oNQcA@Mjh51@neHJob9Ww/Y#UK6>UL8PU*BqXVOv7%IQW!CVvSwk-;*5jqePBSy'>Mi<8l8SjlWI'AL8@8qQWCyE3iV0sTlTSs,Fp!g62fXW'|2OGqxX*aFTD3YdbtA$h$HHD-wkIT15r#XMu$Ggpfcr%F4=@MS%qUrcV=%ftscK,+-f%2;4X6KWMeD#TBoGn/LyMm8E=kX-Y,QVyA-UCVh|1k=@y>5ms<v>I5OhpG#MA@42nW7!,U5=wW1*j6a8oH5N|dD<+Rhh4#5Twvq4Ti*eZ4Gw7oUs<buy-9E,kHa=e3QBHl&MdTGYAo3=t@!Fwb0zcloS8sKNQBm2JwkY6um7Kah$u2SFX1+%V64Agks&k/z|2%P-L8<U2h/4Rr!6&@0aDvJEs8$TmxNAR=fr*@7,9fuFFaMS<n@=phCPl%CKP-K-cUQqpqwLeC@NW5qS!AR&2HFvGa$bV=HPV9PySaMn5|%WUpZ0235MINkfiJC|5ZvHq,EwOofxgRwKSx'qXTw/%gb>A1NizdDr',3X,>MeF$EH<Z1Nnxubq>l,tr@Pwe82o!/k<%>M%2'0iEzdA7|-xW-d'jvX9jIw4<g9S-WDuSX4cMR'M5*y4uMSj7SZlejeQRb'T|H&y,g,&3p%KVtR6$r1AMjPQviMAW=2SOr6n|x=%tnCdP+yGGPI,1j;g6Io'UnMB40Rq1TY6O<-Ub'tpTByreIL5/XVz$Y>Ykf-=/9Vt3IS!lE=+|4Y$Rt9WRhq5cmTrNkFM%0K@;G2U=N<xXo!gKX2%njXBIx1O2Af;fi1nh''dS4%@G'jo4RCMC,U/B<Q1pRKwyD7HUH#hfGejyAau6!D5*p%@*&dgHo++XQwVo;U/DGQOXQ%SZYdek1$4P2oGjNm=,JO-ovq1/G7rT#6AM3VdbBEoQ=IrSH4+NuRiw3BitCIagXGBphRUHLpg$!/XgOKjQmL0sVeM628hEwW<h2JEa8o6E2OCfro@!SMq<z2GDYChYW*6oE=BBrP,K7XtSwuN!hLsen!8J0B9je,>C9>XF2FQd2Kf>fw&L0vVpy'sV0aWZN*5#4zVl'j,5kFO2ap9ko57Utcd@vQT&l7k9cmgz,hLHD6YbP$c$O>RYqCmrP|TP1ydE1up9ADEcdlck2KJ4-2E8v6e3m@J1bfG1hv>yp!iivS2/6045S*j3*<$5gTKSKeqgm!SAzIe$@<V1$mFY*XsWK'o#g8#*Ye,9>52#GOLSTcu8X>7dpBg;>5CRLf9$o3ub*aS>%X00W!W|-fnFLLvx4lk$u,4d+7Nr#pi/tmq>s@+G*f8NtoG/,fSe7!HP4Z7yCH%zPxtwK-fz17jZCJ%H=X<EP;iX,3I$0bk7MhUjP5di>C2gg03i3ErAedPB7HKeA'Zo*dKCx4H2ig1y#TWu7;uwM471%@%Q=L@Kk&-'Dx<t+=UArp#*+g5d>gn!6MegAuAAg$U1Kmlwx'<J03Pv$rpMx#@k#$ZG|5KgpyTpJHi@=5br9A!dMVK%zQUqmgx7Bd4+TD'kntey9l=e;%>$q0M7Bp0IIzDGHp#O6V7@6J!$oqLqoe/oW@zYnOApvaf841LD2S1v%V-;0E|06>xT>3l>BC+DFa#NH|L='VBy1KFv@<3Z$pZNF36Vvj!Wyg;k;FK;qMI-uc*g4|LMmUz@N/L%#Pkkl|79ULW&Aqzg#dUA9r=zaELirB+kIZne!-OdFWz#rTSh',pmugVnfyX*aI'<jWsxjOL1L=2Kn32JWXT!W>dKGl>piskzyqrE1QW</hxNBEgnn/M'3xB*,kEy!jqVdUDePKDuS<-CRi+UcjFXPdo0Cw9KF2Bub7aI*Cn;WSirusEvV5&$@LpqrmiIGXhaX$u;n<1Byj;yJF9mAB@x0wYP</gP2FTithIR7/Vs8PYXkp&A2yey4f#%sTjjK,2bDwNB>EbrXz5OPf&cNe1uDV%s>CV95llcX$,1N;ffj4Kp@K@@SiJdpElB,9tE8LdD6%Y&4dGAQx,DoghYfjekUM-Z*U*|2wK=tP+HP3E8-C5iH0WI608lBY$!BxHhV*Na3zmtQ'#tQ$JFI|;$%G#-e3zGOaSR5;DV%l>@K3X<!o+YmU!auv<sQ;MhIgF+Rzrl*vOazT>,oZHDBOwY4>>cW'uzC%cE1'Bodrc7xrDvmGp8-ma3hLBuozoeUbSGf28|FfP2liMXIt'n!jD@KT+aUP&LK>46Y*>MJah40DyWU$A,Su0<ShMO|ijU/g,56vFHhc#r8&/zD>fMe<r/Bl2;--6tDZ&12<@lE6;0Gt!pxO6Ugs'e=pbIRc&CdpyBUvnf'&OweS2TXigM5iU;t|WVD<%sYXLo&xrknC#r$=xh2!TOZD1i9b@K8Nod5F8ErMyDW2DrWg|5p6#QgY-<C>0%7=2|c*D57<2,m5b73K0TQMT-Sxo2+o+L$2-A6|5vXzD'eA9WXnMJe6r|qr$1V%#'ZW*QbfttL5t!;F2o7ME%30,,l+1#wN;$&HZpj'K#C%PU'4lQ9C5KVYo9kfs>vDB82-BC*Q+oz6-sMV8pednyV8qb&FrkhvJ,#o<lJ4CR*33ArDR#/UQk8SOcz4eDn&X3Qa$|uSKLBb7YUbq-!1Q+aynSR7UlMe6J0xRF'-zqz,pBzCquY-bxoBeIzvKIXvCA3b0Lc9e9ZorvbIWnb1&RkqHGz$vbZ%Fj'Ai!'rDFyK$a;O8UfAdgj+LX,Lql5B6x<I5';0K&9RE9X9dEc+<+hHmMbKVmU<W1Br8A78Y+tilJy+vbB$OyQYF*$W'2gCb%g;0UVLkSXtJd42n>/dw!RwD8WPi=t#oEhPCaa*prgmomLaIJdCMU/w5k$dGL1$WYJgJi<DX=h%7JXz1Vi'ogMe1!isYx;0A40u!hE%7#,-tR'!1%8B;dl|Nu7cHWYe+6C|KH3fv0Ap/Ac/!*,Yz>-|cvj4VP%<VP@=jdqn5T-&P%!2!tJSwi3XOOL<W1D,RRR/I;XFt5'm#Or8X@;X'jLD=Nuk;2kMQnz0#j4G$Pkd2|Y9csXw!vth%AV4z6fFkb5XnF=I$l'd*5Pq2>Qq#0'a8gZ@s73TTFcewr;<l|+WUC#bDhtw3gU-EgUtk$+OWG,TWe$sGH=4qFW=PD9O571zsquxT=h+u4cxrIvkgcw7Ujs,B,34j,*Ss@UAMFxGWZri=v=s3H#=ZsEZj&V#Qm$Rl+Jy-27m,31|*f+=J#=OsS8kB5NWIcLYiT'56E|i$|9'I;resUo=kXnvpuB,bgAk9/0Zt+>&|OvvvUhe$|xgZ=19!4AL-a=BUMe$X$R@2ZBb3t7LaM<#tsmsyg8N$=tc>c@Q=/B$r-Um&oOB+3'K%q|P>W/3R+,Q$!6cH0k+P$,Dt<ollqzsiy#@-0,lrLd$ZUtXsTw=NMY;+gj!9u$t;ifvp*XW%>vZWJryCmg@dj2yy=xqz=H6fYJ;oKt3Y%lWK5&rAmq9yl*Pt3j';wO+mdKZt5sC5GBU|nI0dmU/!IV-8;Z>S!s80/AAET4I/|!R;W!mnaAmB30DKiYf4=pP/fwLjpTGgHw<feOdm5eeNJBSE1vE2D9uQBUec,FDnZV=kJaCW0rTdZ+2M2LfP-*%q8QMf!%eEk/NNkK<,1jZdyNr2;ta6>NnqqwDHz<G&|F$+A-LbArWdu=!oCWFcnG=B=2d6Jh896F$GuIney>e#S3EJP@5xJ%W=FnBUzW@7QqW2,2N9OLlpPrkwfABI*M!/K|84S0Hx<r'mG+Bjm-esdLM=ZYrsBQjyZybf7FNw99>uU;=lwJbSJL+@GEqq/2;V'Fu78vPxd5ev*M|%@f$|GwbpDVnzZ$34wEahJx4HI,9yi1M|;g/Mq2$g>,zb1YVNHYIo<J=xn0LFWFfh<pVdH//tq-Q;UrFL4-XA@BlKe-cKqc>jAVF49!jq9pDH;veumhwMahR8+8@W&7G2E,|VQde*%U%gXOu1d;O2Ge=gqpxh$T6G-bH1LLmNvP%0GBgtDdphR3CFMd%y7tl-5J7gITu$<VQR1iDrCY<XY%PqFXMh1i-h7UqDIxG9'D9i0oiOLuSvtN@6SQ0ALA&V9V'#v&SYw'5@G=Krry6kG!r<$1;15e#@qRBjEqt'TRmZFV5Z8n+6WMi/PN|18SqJoSKf-OPy/0tcPxX7YDsU#WrsKgr@D4V%ae'Nz-8aEjIPqS*,eux8h>BkA9X=V%EX'=ZuBI808krGC2PQ4GM#5>=ZD6>TQ+#vAkX<0k#&Nd-;g|!@;dL'Iefbzk9H2grbtR-R#gH%*WhqmEoO|nsBr$kzv7byhsr=YmaO2YI-M-SXc'SgfqztW6pp7H*Mupe&zRtx'O*vtd2jQQ<w0Mft@H@|2ceqR;oH<RUQ>clUY'c3IUm|M*X|1b3OA!TgAx9=$<#zpnmwT&/NO6$M9FL|t@R1gMkw$#91nxCkbt-THt3<6gKPOtX$DtayZmuY9dAb,|eH90DdA1sDeS@>oMJr,rG5<bMsT/!M7T8Bz12,%mW8EWauK+>C+y&m>6EzMYLAk8Ktdx-/|i%*R/&=MuR&V;u*,zPhce7TkZ$nb<<hd2cPlmPqpYriD$J!b/xdta3sn2-S8rnmh>EO#CNr'c8=YG53Z#UH6l98EMaSAETrbDxi/f2Iv>kj8NiBt!2!0O7@8yFP@nbNVlZNfUT2CWjkckY/OoE$ZBwnEBFV4PF#Y@34PZT6sGl!%&4CY;Y2=tu%KoAJ9P7hARFq/<zZbLx4FBiQRaSL>yxKyLZ*fdqr#sW8-7%7>3&b<8>2RoL@@|H#=P9'CTWdz+;XUdesDqC@B$i,>X&vDgohs79AKbKUB+Oi-J=C66$i!eTF!JIr%+R%mXSF9X#<CBK/vonLTGVR,faVb3BOVlk*+d|I/@|@SV>S1ZJ$P@*uGEK-23-uUMJZe'4<Cm*+2e!Py;*vL9FbF+0*VfDq|8poCr6qs5P@3*c8YMQZL9,YAwIW0%;5sm>6kWseD=,SpLAl%n'w!4$+65L7;+S8g8jA$KC%WLO<|,r;u1@+G#HFBBPD'ToZ,#1qeRd6j3$aT|4l-6xA'aoNK/jBt1li2YjcpZxumn''dIYG,@|xD*BDdiPz=u%JYJHdlA*7fvg3'J>YoXNTz3HqfIWiutfS<;51@CI@ZQHvy&5k3|lJQHul'TOu5Scwvmo>DKPAFP,5CD7t5Du9O2=bI2KIsJ*JTvsXo+5M/TR$kM/CrR&65hS&t2h1jMWs>S'-cbc0,S+kn;qTgL5lkbzSXL|h|v#t<%,pdZIPb5v3yHfGFe,cLW2=IParqkrXdUP@$z&*tPH$OvDoXOEQZlL-y7a8i&s1Kn7BaD9/4T=XqDm5/e8,q>f5Q-nHpqF%Q8ZLcW2#2Z'=JlDCdhbp1Ct$T%8QBm;174=muuGCla,42%<kclD5!%e-k<W&kb9xc$ioGyuAq8xrq8A*woZ2yfsITD=gReuYT1Ft#/#iB-uW@&;JgAEwZ38724QNJ>&Q=1@VP#U%Y&K!3'-SZ62o/l#4oW*!,E6|Y!mK4B->lzLV6dYf&4vdG%t*x+dW6kfDM=&;Xcj--hr04LCbx<w6Z=I7dnNE#ygK'Mt<=-FzR,ohv7>oQ*4LPOCZ%H<vgXPXZWsug|cvvZfvAEZ6%p4tR;kEyFnoWDs1,Y$koUEbz6z10cM5&LsQo/pEzaHiZ<D51>xW1I#O-VyRsu'If*OIpsWLM7z&AS;5fDx1C'=Y/dhJcVLb;lLHdD=$6B$6;b3L,V|rLx!,m7g'bTLf/Sl>Z8fzmBd%JX>ICX8k@G>heW*rtSi-a1NM;uS,|UDHT+%HhV$Re@JgrM<oWzG1XT>p7!vP$UxJ@Z%GX&u/%oScK7";
    //PASS PHRASE
    public static final byte[] PASS_PHRASE = passwordString.getBytes(); //Database password

    //TABLES
    private static final String USER_TABLE_NAME = "User";
    private static final String MEDIA_TABLE_NAME = "Media";
    private static final String NOTE_TABLE_NAME = "Note";
    private static final String TAG_TABLE_NAME = "Tag";
    private static final String SET_TAGS_TABLE_NAME = "SetTags";
    private static final String ALL_TASKS_TABLE_NAME = "AllTasks";
    private static final String EXCEEDED_TASKS_TABLE_NAME = "ExceededTasks";
    private static final String IN_PROGRESS_TASKS_TABLE_NAME = "InProgressTasks";
    private static final String COMPLETED_TASKS_TABLE = "CompletedTasks";

    //USER DATA
    private static final String USER_ID = "user_id";
    private static final String USER_USERNAME = "user_username";
    private static final String USER_PASSWORD = "user_password";
    private static final String USER_FULL_NAME = "user_fullName";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_PHONE_NUMBER = "user_phone_number";
    private static final String USER_GOOGLE_SIGN_IN = "user_google_sign_in";
    private static final String USER_WORK_TIME = "user_work_time";
    private static final String USER_FREE_TIME = "user_free_time";
    private static final String USER_SAVE_POMODORO_AS_TASK = "user_save_pomodoro_as_task";
    private static final String USER_PROFILE_IMAGE = "user_profile_image";

    //IMAGES/VIDEOS URI's
    private static final String MEDIA_ID = "media_id";
    private static final String MEDIA_BLOB = "media_blob";

    //NOTE DATA
    private static final String NOTE_ID = "note_id";
    private static final String NOTE_TITLE = "note_title";
    private static final String NOTE_TEXT = "note_text";
    private static final String NOTE_DATE = "note_date";
    private static final String NOTE_CATEGORY1 = "note_cat1";
    private static final String NOTE_CATEGORY2 = "note_cat2";
    private static final String NOTE_CATEGORY3 = "note_cat3";
    private static final String NOTE_MEDIA_BITMAPS = "note_media_bitmaps";

    //TAG
    private static final String TAG_ID = "tag_id";
    private static final String TAG_CATEGORIES = "tag_categories";
    private static final String TAG_COLOR = "tag_color";

    //SET_TAGS
    private static final String SET_TAGS_ID = "id";
    private static final String SET_TAGS_VALUE_ID = "setTag_id";

    //TASK
    private static final String TASK_ID = "task_id";
    private static final String TASK_TITLE = "task_title";
    private static final String TASK_TEXT = "task_text";
    private static final String TASK_PROGRESS = "task_progress";
    private static final String TASK_CATEGORY1 = "task_cat1";
    private static final String TASK_CATEGORY2 = "task_cat2";
    private static final String TASK_CATEGORY3 = "task_cat3";
    private static final String TASK_START_DATE = "task_startDate";
    private static final String TASK_END_DATE = "task_endDate";
    private static final String TASK_MEDIA_BITMAPS = "task_media_bitmaps";

    //EXCEEDED_TASKS
    private static final String EXCEEDED_ID = "exceeded_id";
    private static final String EXCEEDED_ID_VALUE = "exceeded_task_id";

    //IN_PROGRESS_TASKS
    private static final String IN_PROGRESS_ID = "in_progress_id";
    private static final String IN_PROGRESS_ID_VALUE = "in_progress_task_id";

    //COMPLETED_TASKS
    private static final String COMPLETED_ID = "completed_id";
    private static final String COMPLETED_ID_VALUE = "completed_task_id";

    //SQLCIPHER LOGIN IMPLEMENTATION
    private static SQLiteDAO instance;
    private Context context;
    public static boolean enableSQLCypher = true;
    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }
    private boolean databaseExists(Context context) {
        File databaseFile = context.getDatabasePath(WTM_DB_NAME);
        return databaseFile.exists();
    }
    public SQLiteDAO(@Nullable Context context) {
        super(context, WTM_DB_NAME, null, WTM_DB_VERSION);
        SQLiteDatabase.loadLibs(context);
        setWriteAheadLoggingEnabled(true);
        setContext(context);
    }
    static public synchronized SQLiteDAO getInstance(Context context) {
        if (instance == null) {
            instance = new SQLiteDAO(context);
            //db = instance.getWritableDatabase(PASS_PHRASE);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!databaseExists(getContext())) {
            db.execSQL("PRAGMA key = '" + PASS_PHRASE + "'");
        }
        String user_Sql_Query = "CREATE TABLE " + USER_TABLE_NAME + "("
                + USER_ID + " INTEGER PRIMARY KEY," + USER_USERNAME + " TEXT,"
                + USER_PASSWORD + " TEXT," + USER_FULL_NAME + " TEXT," + USER_EMAIL + " TEXT," + USER_PHONE_NUMBER + " TEXT," + USER_GOOGLE_SIGN_IN + " TEXT," + USER_WORK_TIME + " TEXT," + USER_FREE_TIME + " TEXT," + USER_SAVE_POMODORO_AS_TASK + " TEXT," + USER_PROFILE_IMAGE + " BLOB" + ")";
        db.execSQL(user_Sql_Query);
        String media_sql_Query = "CREATE TABLE " + MEDIA_TABLE_NAME + "("
                + MEDIA_ID + " INTEGER PRIMARY KEY," + MEDIA_BLOB + " BLOB" + ")";
        db.execSQL(media_sql_Query);
        String note_Sql_Query = "CREATE TABLE " + NOTE_TABLE_NAME + "("
                + NOTE_ID + " INTEGER PRIMARY KEY," + NOTE_TITLE + " TEXT,"
                + NOTE_TEXT + " TEXT," + NOTE_DATE + " TEXT," + NOTE_CATEGORY1 + " TEXT," + NOTE_CATEGORY2 + " TEXT," + NOTE_CATEGORY3 + " TEXT," + NOTE_MEDIA_BITMAPS + " TEXT" + ")";
        db.execSQL(note_Sql_Query);
        String tag_sql_Query = "CREATE TABLE " + TAG_TABLE_NAME + "("
                + TAG_ID + " INTEGER PRIMARY KEY," + TAG_CATEGORIES + " TEXT,"
                + TAG_COLOR + " TEXT" + ")";
        db.execSQL(tag_sql_Query);
        String set_tags_sql_Query = "CREATE TABLE " + SET_TAGS_TABLE_NAME + "("
                + SET_TAGS_ID + " INTEGER PRIMARY KEY," + SET_TAGS_VALUE_ID + " TEXT" + ")";
        db.execSQL(set_tags_sql_Query);
        String task_Sql_Query = "CREATE TABLE " + ALL_TASKS_TABLE_NAME + "("
                + TASK_ID + " INTEGER PRIMARY KEY," + TASK_TITLE + " TEXT,"
                + TASK_TEXT + " TEXT," + TASK_PROGRESS + " TEXT," + TASK_CATEGORY1 + " TEXT,"
                + TASK_CATEGORY2 + " TEXT," + TASK_CATEGORY3 + " TEXT," + TASK_START_DATE + " TEXT," + TASK_END_DATE + " TEXT," + TASK_MEDIA_BITMAPS + " TEXT" + ")";
        db.execSQL(task_Sql_Query);
        //
        String exceeded_sql_Query = "CREATE TABLE " + EXCEEDED_TASKS_TABLE_NAME + "("
                + EXCEEDED_ID + " INTEGER PRIMARY KEY," + EXCEEDED_ID_VALUE + " TEXT" + ")";
        db.execSQL(exceeded_sql_Query);
        String in_progress_sql_Query = "CREATE TABLE " + IN_PROGRESS_TASKS_TABLE_NAME + "("
                + IN_PROGRESS_ID + " INTEGER PRIMARY KEY," + IN_PROGRESS_ID_VALUE + " TEXT" + ")";
        db.execSQL(in_progress_sql_Query);
        String completed_sql_Query = "CREATE TABLE " + COMPLETED_TASKS_TABLE + "("
                + COMPLETED_ID + " INTEGER PRIMARY KEY," + COMPLETED_ID_VALUE + " TEXT" + ")";
        db.execSQL(completed_sql_Query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MEDIA_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TAG_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SET_TAGS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ALL_TASKS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EXCEEDED_TASKS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IN_PROGRESS_TASKS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + COMPLETED_TASKS_TABLE);
        onCreate(db);
    }

/*----------------------------------------User----------------------------------------------------*/
public void addUser(User new_user){
    SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
    ContentValues contentValues = new ContentValues();
    contentValues.put(USER_USERNAME,new_user.getUserUsername());
    contentValues.put(USER_PASSWORD, new_user.getUserPassword());
    contentValues.put(USER_FULL_NAME, new_user.getUser_full_name());
    contentValues.put(USER_EMAIL, new_user.getUser_email());
    contentValues.put(USER_PHONE_NUMBER, new_user.getUser_phone_number());
    contentValues.put(USER_GOOGLE_SIGN_IN, Boolean.toString(new_user.getUser_google_sign_in()));
    contentValues.put(USER_WORK_TIME, new_user.getWork_time());
    contentValues.put(USER_FREE_TIME, new_user.getFree_time());
    contentValues.put(USER_SAVE_POMODORO_AS_TASK, Boolean.toString(new_user.isSave_pomodoro_as_task()));
    contentValues.put(USER_PROFILE_IMAGE, new_user.getProfile_image());

    db.insert(USER_TABLE_NAME,null,contentValues);
    db.close();
}

    public int addUserGetID(User new_user) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_USERNAME,new_user.getUserUsername());
        contentValues.put(USER_PASSWORD, new_user.getUserPassword());
        contentValues.put(USER_FULL_NAME, new_user.getUser_full_name());
        contentValues.put(USER_EMAIL, new_user.getUser_email());
        contentValues.put(USER_PHONE_NUMBER, new_user.getUser_phone_number());
        contentValues.put(USER_GOOGLE_SIGN_IN, Boolean.toString(new_user.getUser_google_sign_in()));
        contentValues.put(USER_WORK_TIME, new_user.getWork_time());
        contentValues.put(USER_FREE_TIME, new_user.getFree_time());
        contentValues.put(USER_SAVE_POMODORO_AS_TASK, Boolean.toString(new_user.isSave_pomodoro_as_task()));
        contentValues.put(USER_PROFILE_IMAGE, new_user.getProfile_image());

        db.insert(USER_TABLE_NAME,null,contentValues);
        String sql_Query = "SELECT MAX(" + USER_ID + ") AS LAST FROM " + USER_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query, null);

        int rowID = -1;
        if(cursor.moveToFirst()){
            rowID = cursor.getInt(0);
        }
        db.close();
        return rowID;
    }

    public int getUserCount(){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + USER_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query, null);

        int count = cursor.getCount();
        db.close();
        return count;
    }

    public User getUserById(int id){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        User user = null;
        Cursor cursor = db.query(USER_TABLE_NAME,new String[]{USER_ID,USER_USERNAME,USER_PASSWORD,USER_FULL_NAME,USER_EMAIL,USER_PHONE_NUMBER,USER_GOOGLE_SIGN_IN, USER_WORK_TIME, USER_FREE_TIME, USER_SAVE_POMODORO_AS_TASK, USER_PROFILE_IMAGE},
                USER_ID + "=?", new String[]{valueOf(id)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            user = new User(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), Boolean.valueOf(cursor.getString(6)), Integer.parseInt(cursor.getString(7)),  Integer.parseInt(cursor.getString(8)), Boolean.valueOf(cursor.getString(9)), cursor.getBlob(10));
        }
        db.close();
        return user;
    }

    public User getUserByEmail(String email){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        try {
            User user = null;
            Cursor cursor = db.query(USER_TABLE_NAME,new String[]{USER_ID,USER_USERNAME,USER_PASSWORD,USER_FULL_NAME,USER_EMAIL,USER_PHONE_NUMBER,USER_GOOGLE_SIGN_IN, USER_WORK_TIME, USER_FREE_TIME, USER_SAVE_POMODORO_AS_TASK, USER_PROFILE_IMAGE},
                    USER_EMAIL + "=?", new String[]{valueOf(email)},null,null,null);

            if(cursor != null){
                cursor.moveToFirst();
                user = new User(cursor.getInt(0),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), Boolean.valueOf(cursor.getString(6)), Integer.parseInt(cursor.getString(7)),  Integer.parseInt(cursor.getString(8)), Boolean.valueOf(cursor.getString(9)), cursor.getBlob(10));
            }
            db.close();
            return user;
        } catch (Exception e) {
            db.close();
            return null;
        }
    }

    public List<User> getAllUsers(){
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + USER_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query,null);

        User user = null;
        if(cursor.moveToFirst()){
            do{
                user = new User();
                user.setID(cursor.getInt(0));
                user.setUserUsername(cursor.getString(1));
                user.setUserPassword(cursor.getString(2));
                user.setUser_full_name(cursor.getString(3));
                user.setUser_email(cursor.getString(4));
                user.setUser_phone_number(cursor.getString(5));
                user.setUser_google_sign_in(Boolean.parseBoolean(cursor.getString(6)));
                user.setWork_time(cursor.getInt(7));
                user.setFree_time(cursor.getInt(8));
                user.setSave_pomodoro_as_task(Boolean.parseBoolean(cursor.getString(9)));
                user.setProfile_image(cursor.getBlob(10));
                userList.add(user);
            }
            while (cursor.moveToNext());
        }

        db.close();
        return userList;
    }

    public void updateUser(User user){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_USERNAME,user.getUserUsername());
        contentValues.put(USER_PASSWORD, user.getUserPassword());
        contentValues.put(USER_FULL_NAME, user.getUser_full_name());
        contentValues.put(USER_EMAIL, user.getUser_email());
        contentValues.put(USER_PHONE_NUMBER, user.getUser_phone_number());
        contentValues.put(USER_GOOGLE_SIGN_IN, Boolean.toString(user.getUser_google_sign_in()));
        contentValues.put(USER_WORK_TIME, user.getWork_time());
        contentValues.put(USER_FREE_TIME, user.getFree_time());
        contentValues.put(USER_SAVE_POMODORO_AS_TASK, Boolean.toString(user.isSave_pomodoro_as_task()));
        contentValues.put(USER_PROFILE_IMAGE, user.getProfile_image());

        db.update(USER_TABLE_NAME,contentValues,USER_ID + " =?", new String[]{valueOf(user.getID())});
        db.close();
    }

    public void deleteUser(User user){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        db.delete(USER_TABLE_NAME, USER_ID + " =?", new String[]{valueOf(user.getID())});
        db.close();
    }

    public void deleteAllUsers(){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        String sql_Query = " DELETE FROM " + USER_TABLE_NAME;

        db.execSQL(sql_Query);
        db.close();
    }

/*---------------------------------------Media----------------------------------------------------*/
    public void addMedia(Media new_media){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEDIA_BLOB, new_media.getMedia_blob());

        db.insert(MEDIA_TABLE_NAME,null,contentValues);
        db.close();
    }

    public int addMediaGetID(Media new_media) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEDIA_BLOB, new_media.getMedia_blob());

        db.insert(MEDIA_TABLE_NAME,null,contentValues);
        String sql_Query = "SELECT MAX(" + MEDIA_ID + ") AS LAST FROM " + MEDIA_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query, null);

        int rowID = -1;
        if(cursor.moveToFirst()){
            rowID = cursor.getInt(0);
        }
        db.close();
        return rowID;
    }

    public int getMediaCount(){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + MEDIA_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query, null);

        int count = cursor.getCount();
        db.close();
        return count;
    }

    public Media getMediaById(int id){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        Media media = null;
        Cursor cursor = db.query(MEDIA_TABLE_NAME,new String[]{MEDIA_ID, MEDIA_BLOB},
                MEDIA_ID + "=?", new String[]{valueOf(id)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            media = new Media(cursor.getInt(0),
                    cursor.getBlob(1));
        }
        db.close();
        return media;
    }

    public List<Media> getAllMedia(){
        List<Media> mediaList = new ArrayList<>();
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + MEDIA_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query,null);

        Media media = null;
        if(cursor.moveToFirst()){
            do{
                media = new Media();
                media.setID(cursor.getInt(0));
                media.setMedia_blob(cursor.getBlob(1));
                mediaList.add(media);
            }
            while (cursor.moveToNext());
        }

        db.close();
        return mediaList;
    }

    public void updateMedia(Media media){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(MEDIA_BLOB,media.getMedia_blob());

        db.update(MEDIA_TABLE_NAME,contentValues,MEDIA_ID + " =?", new String[]{valueOf(media.getID())});
        db.close();
    }

    public void deleteMedia(Media media){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        db.delete(MEDIA_TABLE_NAME, MEDIA_ID + " =?", new String[]{valueOf(media.getID())});
        db.close();
    }

    public void deleteAllMedia(){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        String sql_Query = " DELETE FROM " + MEDIA_TABLE_NAME;

        db.execSQL(sql_Query);
        db.close();
    }

/*---------------------------------------Notes----------------------------------------------------*/
    public void addNote(Note new_note){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_TITLE,new_note.getNoteTitle());
        contentValues.put(NOTE_TEXT, new_note.getNoteText());
        contentValues.put(NOTE_DATE, new_note.getNoteDate());
        contentValues.put(NOTE_CATEGORY1, new_note.getCategory1());
        contentValues.put(NOTE_CATEGORY2, new_note.getCategory2());
        contentValues.put(NOTE_CATEGORY3, new_note.getCategory3());
        contentValues.put(NOTE_MEDIA_BITMAPS, new_note.getMediaBitmaps());

        db.insert(NOTE_TABLE_NAME,null,contentValues);
        db.close();
    }

    public int addNoteGetID(Note new_note) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_TITLE,new_note.getNoteTitle());
        contentValues.put(NOTE_TEXT, new_note.getNoteText());
        contentValues.put(NOTE_DATE, new_note.getNoteDate());
        contentValues.put(NOTE_CATEGORY1, new_note.getCategory1());
        contentValues.put(NOTE_CATEGORY2, new_note.getCategory2());
        contentValues.put(NOTE_CATEGORY3, new_note.getCategory3());
        contentValues.put(NOTE_MEDIA_BITMAPS, new_note.getMediaBitmaps());

        db.insert(NOTE_TABLE_NAME,null,contentValues);
        String sql_Query = "SELECT MAX(" + NOTE_ID + ") AS LAST FROM " + NOTE_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query, null);

        int rowID = -1;
        if(cursor.moveToFirst()){
            rowID = cursor.getInt(0);
        }
        db.close();
        return rowID;
    }

    public int getNoteCount(){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + NOTE_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query, null);

        int count = cursor.getCount();
        db.close();
        return count;
    }

    public Note getNoteById(int id){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        Note note = null;
        Cursor cursor = db.query(NOTE_TABLE_NAME,new String[]{NOTE_ID,NOTE_TITLE,NOTE_TEXT,NOTE_DATE,NOTE_CATEGORY1,NOTE_CATEGORY2,NOTE_CATEGORY3, NOTE_MEDIA_BITMAPS},
                NOTE_ID + "=?", new String[]{valueOf(id)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            note = new Note(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        }
        db.close();
        return note;
    }

    public List<Note> getAllNotes(){
        List<Note> noteList = new ArrayList<>();
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + NOTE_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query,null);

        Note note = null;
        if(cursor.moveToFirst()){
            do{
                note = new Note();
                note.setID(cursor.getInt(0));
                note.setNoteTitle(cursor.getString(1));
                note.setNoteText(cursor.getString(2));
                note.setNoteDate(cursor.getString(3));
                note.setCategory1(cursor.getString(4));
                note.setCategory2(cursor.getString(5));
                note.setCategory3(cursor.getString(6));
                note.setMediaBitmaps(cursor.getString(7));
                noteList.add(note);
            }
            while (cursor.moveToNext());
        }

        db.close();
        return noteList;
    }

    public int updateNote(Note note){
        deleteNote(note);
        return addNoteGetID(note);
    }
    public void updateNoteSamePosition(Note note){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_TITLE, note.getNoteTitle());
        contentValues.put(NOTE_TEXT, note.getNoteText());
        contentValues.put(NOTE_DATE, note.getNoteDate());
        contentValues.put(NOTE_CATEGORY1, note.getCategory1());
        contentValues.put(NOTE_CATEGORY2, note.getCategory2());
        contentValues.put(NOTE_CATEGORY3, note.getCategory3());
        contentValues.put(NOTE_MEDIA_BITMAPS, note.getMediaBitmaps());

        db.update(NOTE_TABLE_NAME,contentValues,NOTE_ID + " =?", new String[]{valueOf(note.getID())});
        db.close();
    }

    public void deleteNote(Note note){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        db.delete(NOTE_TABLE_NAME, NOTE_ID + " =?", new String[]{valueOf(note.getID())});
        db.close();
    }

    public void deleteAllNotes(){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        String sql_Query = " DELETE FROM " + NOTE_TABLE_NAME;

        db.execSQL(sql_Query);
        db.close();
    }
    public void generateNotesMock(){
        if(getNoteCount() == 0){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM dd, HH:mm");
            Date date = new Date();
            addNote(new Note(1,"Reasons why TO-DO lists can be overwhelming.","One of the reasons to-do lists get so overwhelming is they tend to contain a random mishmash of everything. You might be working on multiple projects at work, and trying to store a reminder to review your upcoming campaign brief next to a note about sourcing vendors for an event can get confusing—fast. No wonder you’re feeling overwhelmed.\n" +
                    "\n" +
                    "To take control of your to-do list and get your best work done, consider making more than one list on your to-do list app, like separate ones for personal and team collaboration projects. For example, make sure each project or large initiative has its own list. Additionally, consider creating one list for work that’s immediately actionable, another for future project ideas, and a third for personal tasks, like a shopping list. That way, you can open the to-do list that’s relevant to the work you’re doing right now, in order to better focus on what you need to get done."
                    , simpleDateFormat.format(date), "important", "work", "school", null));
            addNote(new Note(2,"Parental expectations (having high expectations for their children) and parental involvement (having parents as active and knowledgeable participants in transition planning) have been identified as evidence-based predictors of improved postschool outcomes for students with disabilities. However, little is known about how education professionals can support and promote high expectations and involvement of families for their transition-aged youth with disabilities. Parent advocates for students with disabilities across the nation were asked for their ideas. The following provides a “to-do” list of seven strategies and 13 activities special education professionals can use in partnership with families to promote high expectations for postschool success for young adults with disabilities.", "Parental expectations (having high expectations for their children) and parental involvement (having parents as active and knowledgeable participants in transition planning) have been identified as evidence-based predictors of improved postschool outcomes for students with disabilities. However, little is known about how education professionals can support and promote high expectations and involvement of families for their transition-aged youth with disabilities. Parent advocates for students with disabilities across the nation were asked for their ideas. The following provides a “to-do” list of seven strategies and 13 activities special education professionals can use in partnership with families to promote high expectations for postschool success for young adults with disabilities."
                    , simpleDateFormat.format(date), "health", "people", null, null));
            addNote(new Note(3,"ANDROID BASED MOBILE APPLICATION DEVELOPMENT and its SECURITY", "Android is a new, next-gen mobile operating system\n" +
                    "that runs on the Linux Kernel. Android Mobile Application\n" +
                    "Development is based on Java language codes, as it allows\n" +
                    "developers to write codes in the Java language. These codes\n" +
                    "can control mobile devices via Google-enabled Java libraries.\n" +
                    "It is an important platform to develop mobile applications\n" +
                    "using the software stack provided in the Google Android SDK.\n" +
                    "Android mobile OS provides a flexible environment for\n" +
                    "Android Mobile Application Development as the developers\n" +
                    "can not only make use of Android Java Libraries but it is also\n" +
                    "possible to use normal Java IDEs. The software developers at\n" +
                    "Mobile Development India have expertise in developing\n" +
                    "applications based on Android Java Libraries and other\n" +
                    "important tools. Android Mobile Application Development\n\n" +
                    "Android applications are written in Java programming\n" +
                            "language. However, it is important to remember that they are\n" +
                            "not executed using the standard Java Virtual Machine (JVM).\n" +
                            "Instead, Google has created a custom VM called Dalvik which\n" +
                            "is responsible for converting and executing Java byte code"
                    , simpleDateFormat.format(date), "important", null, null, null));
            addNote(new Note(4, "From information security to cyber security", "Abstract\n" +
                    "\n" +
                    "The term cyber security is often used interchangeably with the term information security. This paper argues that, although there is a substantial overlap between cyber security and information security, these two concepts are not totally analogous. Moreover, the paper posits that cyber security goes beyond the boundaries of traditional information security to include not only the protection of information resources, but also that of other assets, including the person him/herself. In information security, reference to the human factor usually relates to the role(s) of humans in the security process. In cyber security this factor has an additional dimension, namely, the humans as potential targets of cyber attacks or even unknowingly participating in a cyber attack. This additional dimension has ethical implications for society as a whole, since the protection of certain vulnerable groups, for example children, could be seen as a societal responsibility."
                    , simpleDateFormat.format(date), "birthday", "people", "entertainment", null));
        }
    }

/*---------------------------------------Tags-----------------------------------------------------*/
    public int getTagCount() {
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + TAG_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query, null);

        int count = cursor.getCount();
        db.close();
        return count;
    }

    public Tag getTagById(int id){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        Tag tag = null;
        Cursor cursor = db.query(TAG_TABLE_NAME,new String[]{TAG_ID,TAG_CATEGORIES,TAG_COLOR},
                TAG_ID + "=?", new String[]{valueOf(id)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            tag = new Tag(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2));
        }
        db.close();
        return tag;
    }

    public Tag getTagByCategory(String category){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        Tag tag = null;
        Cursor cursor = db.query(TAG_TABLE_NAME,new String[]{TAG_ID,TAG_CATEGORIES,TAG_COLOR},
                TAG_CATEGORIES + "=?", new String[]{valueOf(category)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            tag = new Tag(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2));
        }
        db.close();
        return tag;
    }

    public ArrayList<Tag> getAllTags() {
        ArrayList<Tag> tagsList = new ArrayList<>();
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + TAG_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query,null);

        Tag tag = null;
        if(cursor.moveToFirst()){
            do{
                tag = new Tag();
                tag.setTagID(cursor.getInt(0));
                tag.setTagCategory(cursor.getString(1));
                tag.setTagColor(cursor.getString(2));
                tagsList.add(tag);
            }
            while (cursor.moveToNext());
        }
        db.close();
        return tagsList;
    }

    public void addTag(Tag new_tag) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
        //setting the current time for the changed date
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_CATEGORIES, new_tag.getTagCategory());
        contentValues.put(TAG_COLOR, new_tag.getTagColor());
        db.insert(TAG_TABLE_NAME,null, contentValues);
        db.close();
    }

    public void updateTag(Tag tag){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_CATEGORIES, tag.getTagCategory());
        contentValues.put(TAG_COLOR, tag.getTagColor());

        db.update(TAG_TABLE_NAME,contentValues,TAG_ID + " =?", new String[]{valueOf(tag.getTagID())});
        db.close();
    }
    public void deleteTag(Tag tag) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        db.delete(TAG_TABLE_NAME, TAG_ID + " =?", new String[]{valueOf(tag.getTagID())});
        db.close();
    }

    public void deleteAllTags(){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        String sql_Query = " DELETE FROM " + TAG_TABLE_NAME;

        db.execSQL(sql_Query);
        db.close();
    }

    public void generateDefaultTags() {
        if(getTagCount() == 0){
            addTag(new Tag(1,"important", "FF0000"));
            addTag(new Tag(2,"health", "36894D"));
            addTag(new Tag(3,"work", "673AB7"));
            addTag(new Tag(4,"people", "EF6EF0"));
            addTag(new Tag(5,"entertainment", "F0EE6E"));
            addTag(new Tag(6,"school", "1A83D7"));
            addTag(new Tag(7,"birthday", "C39D2E"));
        }
    }

/*--------------------------------------Tasks-----------------------------------------------------*/
    public void addTask(Task new_task){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_TITLE, new_task.getTaskTitle());
        contentValues.put(TASK_TEXT, new_task.getTaskText());
        contentValues.put(TASK_PROGRESS, new_task.getTaskProgress());
        contentValues.put(TASK_CATEGORY1, new_task.getCategory1());
        contentValues.put(TASK_CATEGORY2, new_task.getCategory2());
        contentValues.put(TASK_CATEGORY3, new_task.getCategory3());
        contentValues.put(TASK_START_DATE, new_task.getTask_startDate());
        contentValues.put(TASK_END_DATE, new_task.getTask_endDate());
        contentValues.put(TASK_MEDIA_BITMAPS, new_task.getMediaBitmaps());

        db.insert(ALL_TASKS_TABLE_NAME,null,contentValues);
        db.close();
    }

    public int addTaskGetID(Task new_task) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_TITLE, new_task.getTaskTitle());
        contentValues.put(TASK_TEXT, new_task.getTaskText());
        contentValues.put(TASK_PROGRESS, new_task.getTaskProgress());
        contentValues.put(TASK_CATEGORY1, new_task.getCategory1());
        contentValues.put(TASK_CATEGORY2, new_task.getCategory2());
        contentValues.put(TASK_CATEGORY3, new_task.getCategory3());
        contentValues.put(TASK_START_DATE, new_task.getTask_startDate());
        contentValues.put(TASK_END_DATE, new_task.getTask_endDate());
        contentValues.put(TASK_MEDIA_BITMAPS, new_task.getMediaBitmaps());

        db.insert(ALL_TASKS_TABLE_NAME,null,contentValues);
        String sql_Query = "SELECT MAX(" + TASK_ID + ") AS LAST FROM " + ALL_TASKS_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query, null);

        int rowID = -1;
        if(cursor.moveToFirst()){
            rowID = cursor.getInt(0);
        }
        db.close();
        return rowID;
    }

    public int getTaskCount(){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + ALL_TASKS_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query, null);

        int count = cursor.getCount();
        db.close();
        return count;
    }

    public Task getTaskById(int id){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        Task task = null;
        Cursor cursor = db.query(ALL_TASKS_TABLE_NAME,new String[]{TASK_ID,TASK_TITLE,TASK_TEXT,TASK_PROGRESS,
                        TASK_CATEGORY1,TASK_CATEGORY2,TASK_CATEGORY3,TASK_START_DATE,TASK_END_DATE, TASK_MEDIA_BITMAPS},
                TASK_ID + "=?", new String[]{valueOf(id)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            task = new Task(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getString(6), cursor.getString(7),
                    cursor.getString(8), cursor.getString(9));
        }
        db.close();
        return task;
    }

    public List<Task> getAllTasks(){
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + ALL_TASKS_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query,null);

        Task task = null;
        if(cursor.moveToFirst()){
            do{
                task = new Task();
                task.setID(cursor.getInt(0));
                task.setTaskTitle(cursor.getString(1));
                task.setTaskText(cursor.getString(2));
                task.setTaskProgress(cursor.getString(3));
                task.setCategory1(cursor.getString(4));
                task.setCategory2(cursor.getString(5));
                task.setCategory3(cursor.getString(6));
                task.setTask_startDate(cursor.getString(7));
                task.setTask_endDate(cursor.getString(8));
                task.setMediaBitmaps(cursor.getString(9));
                taskList.add(task);
            }
            while (cursor.moveToNext());
        }

        db.close();
        return taskList;
    }

    public int updateTask(Task task){
        deleteTask(task);
        return addTaskGetID(task);
    }

    public void updateTaskSamePosition(Task task){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_TITLE, task.getTaskTitle());
        contentValues.put(TASK_TEXT, task.getTaskText());
        contentValues.put(TASK_PROGRESS, task.getTaskProgress());
        contentValues.put(TASK_CATEGORY1, task.getCategory1());
        contentValues.put(TASK_CATEGORY2, task.getCategory2());
        contentValues.put(TASK_CATEGORY3, task.getCategory3());
        contentValues.put(TASK_START_DATE, task.getTask_startDate());
        contentValues.put(TASK_END_DATE, task.getTask_endDate());
        contentValues.put(TASK_MEDIA_BITMAPS, task.getMediaBitmaps());

        db.update(ALL_TASKS_TABLE_NAME,contentValues,TASK_ID + " =?", new String[]{valueOf(task.getID())});
        db.close();
    }

    public void deleteTask(Task task){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        db.delete(ALL_TASKS_TABLE_NAME, TASK_ID + " =?", new String[]{valueOf(task.getID())});
        db.close();
    }

    public void deleteAllTasks(){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        String sql_Query = " DELETE FROM " + ALL_TASKS_TABLE_NAME;

        db.execSQL(sql_Query);
        db.close();
    }
    public void generateTasksMock(){
        if(getTaskCount() == 0){
            Calendar startTime = GregorianCalendar.getInstance();
            Calendar endTime = GregorianCalendar.getInstance();

            startTime.add(Calendar.HOUR_OF_DAY, -25);
            endTime.add(Calendar.HOUR_OF_DAY, -24);
            addTask(new Task(1,"Parental expectations (having high expectations for their children) and parental involvement (having parents as active and knowledgeable participants in transition planning) have been identified as evidence-based predictors of improved postschool outcomes for students with disabilities. However, little is known about how education professionals can support and promote high expectations and involvement of families for their transition-aged youth with disabilities. Parent advocates for students with disabilities across the nation were asked for their ideas. The following provides a “to-do” list of seven strategies and 13 activities special education professionals can use in partnership with families to promote high expectations for postschool success for young adults with disabilities.", "Parental expectations (having high expectations for their children) and parental involvement (having parents as active and knowledgeable participants in transition planning) have been identified as evidence-based predictors of improved postschool outcomes for students with disabilities. However, little is known about how education professionals can support and promote high expectations and involvement of families for their transition-aged youth with disabilities. Parent advocates for students with disabilities across the nation were asked for their ideas. The following provides a “to-do” list of seven strategies and 13 activities special education professionals can use in partnership with families to promote high expectations for postschool success for young adults with disabilities."
            , "Exceeded", "important", "people", "school",
                    String.valueOf(startTime.getTimeInMillis()), String.valueOf(endTime.getTimeInMillis()), null) );
            addExceeded(new TaskType(1, 1));
            //
            startTime = GregorianCalendar.getInstance();
            endTime = GregorianCalendar.getInstance();
            startTime.add(Calendar.MINUTE, -15);
            endTime.add(Calendar.MINUTE, 30);
            addTask(new Task(2,"Proritize your work Today", "    Sorting and prioritizing work. If you want to change the order of your written to-do list, you have to rewrite the whole thing. But with a to-do list app, you can easily drag and drop items. Not only that—most to-do list apps offer a way to track priority with custom tags. Digital to-do lists also allow you to set up recurring tasks, so you’ll never forget a weekly meeting again. Plus, to-do list apps support multiple views, so you can visualize your tasks the way that works best for you, be it in a list or a Kanban board.\n" +
                    "\n" +
                    "    Impossible to lose. Unlike a handwritten to-do list, you can’t “lose” an online to-do list. You’ll always have access to the list—whether on your desktop, iPhone, iPad, or other smart devices—so you can jot down to-dos wherever you are.\n" +
                    "\n" +
                    "    Add additional context to your to-dos. Most to-do list apps offer a way for you to add additional information in the description. In a written to-do list, all you have are a couple of words to describe what you need to work on. But with a to-do list app, each to-do has an expandable description, where you can add any relevant task details, working docs, or important information. Plus, to-do list apps utilize integrations like Google Drive, Google Calendar, and Outlook, so you can attach documents and add context to your most important tasks.\n" +
                    "\n" +
                    "    Create separate lists in the same place. Before you choose a to-do list app, make sure you can create more than one “list” in the app. You might want to create a personal to-do list for your work, another for your team’s work, and a third for your professional development, for example. Or, you may want to sort tasks by timeframe, such as creating a daily to-do list and a weekly to-do list. A to-do list app with multiple list options allows you to store all of these to-dos in one place.\n" +
                    "\n" +
                    "    Set reminders, due dates, and notifications. Your to-dos, whether for a “me” day or critical project deadlines, don’t mean much if they’re not done in time. With a to-do list app, you can track when work is due, and set reminders or customizable notifications to make sure you get your to-dos done in time.\n" +
                    "\n" +
                    "Collaboration. When your individual to-do list is organized and your priorities are clear, you can better contribute to team projects and initiatives. In other words, the more organized you are, the easier it’ll be to collaborate with your team."
            , "Completed", "important", "work", null,
                    String.valueOf(startTime.getTime().getTime()), String.valueOf(endTime.getTime().getTime()), null) );
            addCompleted(new TaskType(1, 2));
            //
            startTime = GregorianCalendar.getInstance();
            endTime = GregorianCalendar.getInstance();
            startTime.add(Calendar.HOUR_OF_DAY, -25);
            endTime.add(Calendar.HOUR_OF_DAY, 23);
            addTask(new Task(3,"Reasons why TO-DO lists can be overwhelming.","One of the reasons to-do lists get so overwhelming is they tend to contain a random mishmash of everything. You might be working on multiple projects at work, and trying to store a reminder to review your upcoming campaign brief next to a note about sourcing vendors for an event can get confusing—fast. No wonder you’re feeling overwhelmed.\n" +
                    "\n" +
                    "To take control of your to-do list and get your best work done, consider making more than one list on your to-do list app, like separate ones for personal and team collaboration projects. For example, make sure each project or large initiative has its own list. Additionally, consider creating one list for work that’s immediately actionable, another for future project ideas, and a third for personal tasks, like a shopping list. That way, you can open the to-do list that’s relevant to the work you’re doing right now, in order to better focus on what you need to get done."
            , "In progress", "entertainment", null, null,
                    String.valueOf(startTime.getTime().getTime()), String.valueOf(endTime.getTime().getTime()), null) );
            addInProgress(new TaskType(1, 3));
            //
            startTime = GregorianCalendar.getInstance();
            endTime = GregorianCalendar.getInstance();
            startTime.add(Calendar.MINUTE, 30);
            endTime.add(Calendar.MINUTE, 45);
            addTask(new Task(4,"TO-DO Today","Wake up to the sound of an alarm.\n" +
                    "Stretch and yawn to greet the day.\n" +
                    "Brush teeth to freshen up.\n" +
                    "Shower or wash up to cleanse.\n" +
                    "Get dressed in clothes for the day.\n" +
                    "Make breakfast, cereal, toast, or coffee.\n" +
                    "Check messages, emails, or social media.\n" +
                    "Commute to work, school, or errands.\n" +
                    "Work on a computer, write, code, or design.\n" +
                    "Attend meetings, classes, or presentations.\n" +
                    "Chat with colleagues or classmates.\n" +
                    "Take a lunch break for a quick bite.\n" +
                    "Run errands, go grocery shopping, or pick up dry cleaning.\n" +
                    "Exercise, go for a walk, run, or hit the gym.\n" +
                    "Cook dinner, prepare a meal for yourself or family.\n" +
                    "Eat dinner, relax and unwind.\n" +
                    "Watch TV, listen to music, or read a book.\n" +
                    "Spend time with family and friends.\n" +
                    "Get ready for bed, shower, and change clothes.\n" +
                    "Go to sleep and prepare for a new day."
            , "Exceeded", "health", "birthday", null,
                    String.valueOf(startTime.getTime().getTime()), String.valueOf(endTime.getTime().getTime()), null) );
            addExceeded(new TaskType(1, 4));
            //
            startTime = GregorianCalendar.getInstance();
            endTime = GregorianCalendar.getInstance();
            startTime.add(Calendar.HOUR_OF_DAY, -25);
            endTime.add(Calendar.MINUTE, -45);
            addTask(new Task(5,"Sibiu-Florance", "Research and book flights from Sibiu to Florence.\n" +
                    "    Check passport validity and any visa requirements for Italy.\n" +
                    "    Book accommodation in Florence.\n" +
                    "    Research things to do and see in Florence.\n" +
                    "    Make a list of essentials to pack for the trip.\n" +
                    "    Pack luggage for the trip to Florence.\n" +
                    "    Arrange transportation to Sibiu airport.\n" +
                    "    Check in for the flight from Sibiu to Florence.\n" +
                    "    Board the plane for Florence.\n" +
                    "    Relax and enjoy the flight to Florence.\n" +
                    "    Land at Florence airport and go through customs.\n" +
                    "Collect luggage and find transportation to your accommodation.\n" +
                    "    Check in at your hotel in Florence.\n" +
                    "    Explore the surrounding area of your hotel.\n" +
                    "Visit a famous landmark in Florence (e.g., Ponte Vecchio, Duomo).\n" +
                    "    Enjoy a traditional Italian meal for lunch.\n" +
                    "Visit a museum or art gallery in Florence (e.g., Uffizi Gallery, Accademia Gallery).\n" +
                    "    Take a walking tour or bike tour of Florence.\n" +
                    "    Sample gelato (Italian ice cream) at a local shop.\n" +
                    "    Relax and people-watch in a beautiful piazza.\n" +
                    "    Do some souvenir shopping at a local market.\n" +
                    "    Enjoy a delicious Italian dinner.\n" +
                    "    Take a night stroll and admire the illuminated Florence.\n" +
                    "    Visit a hidden gem or off-the-beaten-path location.\n" +
                    "    Take a day trip to a nearby town like Siena or Pisa.\n" +
                    "    Enjoy a cooking class and learn to make Italian dishes.\n" +
                    "    Take a wine tasting tour in the Tuscan countryside.\n" +
                    "    Attend a concert or opera performance (if available).\n" +
                    "    Relax and have a spa treatment.\n" +
                    "Write postcards or send messages to loved ones back home.\n" +
                    "    Pack your luggage for the return flight.\n" +
                    "    Check out of your hotel in Florence.\n" +
                    "    Find transportation to Florence airport.\n" +
                    "Check in for the return flight from Florence to Sibiu.\n" +
                    "    Board the plane for Sibiu.\n" +
                    "    Relax and enjoy the flight back to Sibiu.\n" +
                    "    Land at Sibiu airport and collect your luggage.\n" +
                    "    Arrange transportation back to your home in Sibiu.\n" +
                    "    Unpack your luggage and organize souvenirs.\n" +
                    "    Reflect on your trip to Florence and make a photo album."
            , "Exceeded", "entertainment", "important", null,
                    String.valueOf(startTime.getTime().getTime()), "-1", null) );
            addExceeded(new TaskType(2, 5));
            //
            startTime = GregorianCalendar.getInstance();
            endTime = GregorianCalendar.getInstance();
            startTime.add(Calendar.HOUR_OF_DAY, -1);
            endTime.add(Calendar.MINUTE, 45);
            addTask(new Task(6,"Have the best grades!", "Pick classes that interest you and align with your major.\n" +
                    "Review your course syllabi to understand expectations and grading.\n" +
                    "Set up a daily or weekly planner to manage your study time.\n" +
                    "Attend all lectures and take clear and concise notes.\n" +
                    "Review your notes after each class to solidify understanding.\n" +
                    "Form a study group with classmates for challenging subjects.\n" +
                    "Find a quiet and distraction-free place to study effectively.\n" +
                    "Develop a study schedule that prioritizes upcoming assignments and exams.\n" +
                    "Utilize campus resources like tutoring centers and writing labs.\n" +
                    "Ask questions and clarify doubts during lectures or office hours.\n" +
                    "Start assignments early to avoid last-minute cramming.\n" +
                    "Break down large projects into smaller, more manageable tasks.\n" +
                    "Manage your time effectively to avoid procrastination.\n" +
                    "Get enough sleep to stay focused and retain information.\n" +
                    "Eat healthy meals and snacks to fuel your brainpower.\n" +
                    "Take breaks during studying to avoid burnout.\n" +
                    "Practice active recall by summarizing key concepts in your own words.\n" +
                    "Use flashcards or mind maps to organize and memorize information.\n" +
                    "Review past exams and quizzes to understand professor's testing style.\n" +
                    "Formulate a test-taking strategy that works best for you (e.g., outlining, reviewing time management).\n" +
                    "Stay calm and focused during exams by managing test anxiety.\n" +
                    "Review your graded assignments to identify areas for improvement.\n" +
                    "Seek feedback from professors or tutors to understand your strengths and weaknesses.\n" +
                    "Celebrate your achievements and milestones throughout the semester.\n" +
                    "Reflect on your study habits and make adjustments as needed.\n" +
                    "Connect with classmates online through forums or study groups.\n" +
                    "Utilize online resources like educational videos or practice problems.\n" +
                    "Explore different learning methods like textbooks, lectures, or podcasts.\n" +
                    "Take advantage of library resources for research and in-depth learning.\n" +
                    "Maintain a positive attitude and believe in your ability to succeed.\n" +
                    "Reward yourself for completing tasks and achieving goals.\n" +
                    "Don't be afraid to ask for help from classmates, professors, or advisors.\n" +
                    "Maintain a healthy work-life balance to avoid stress and burnout.\n" +
                    "Advocate for yourself and communicate any challenges you face in class.\n" +
                    "Prioritize your well-being and mental health for optimal academic performance.\n" +
                    "Take advantage of professor office hours to discuss course material.\n" +
                    "Explore internship or research opportunities to gain practical experience.\n" +
                    "Network with professors and build relationships for future recommendations.\n" +
                    "Stay updated on academic deadlines and submission requirements.\n" +
                    "Celebrate your accomplishments at the end of the semester and enjoy the break!"
            , "Completed", "school", "important", null,
                    String.valueOf(startTime.getTime().getTime()), String.valueOf(endTime.getTime().getTime()), null) );
            addCompleted(new TaskType(2, 6));
        }
    }

/*----------------------------------ExceededTasks-----------------------------------------------*/
public int getExceededCount() {
    SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

    String sql_Query = "SELECT * FROM " + EXCEEDED_TASKS_TABLE_NAME;
    Cursor cursor = db.rawQuery(sql_Query, null);

    int count = cursor.getCount();
    db.close();
    return count;
}

    public TaskType getExceededByID(int id){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        TaskType taskType = null;
        Cursor cursor = db.query(EXCEEDED_TASKS_TABLE_NAME,new String[]{EXCEEDED_ID,EXCEEDED_ID_VALUE},
                EXCEEDED_ID + "=?", new String[]{valueOf(id)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            taskType = new TaskType(cursor.getInt(0),
                    cursor.getInt(1));
        }
        db.close();
        return taskType;
    }

    public TaskType getExceededByValue(int value){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        TaskType taskType = null;
        Cursor cursor = db.query(EXCEEDED_TASKS_TABLE_NAME,new String[]{EXCEEDED_ID,EXCEEDED_ID_VALUE},
                EXCEEDED_ID_VALUE + "=?", new String[]{valueOf(value)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            taskType = new TaskType(cursor.getInt(0),
                    cursor.getInt(1));
        }
        db.close();
        return taskType;
    }

    public ArrayList<TaskType> getAllExceeded() {
        ArrayList<TaskType> exceededList = new ArrayList<>();
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + EXCEEDED_TASKS_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query,null);

        TaskType counter = null;
        if(cursor.moveToFirst()){
            do{
                counter = new TaskType();
                counter.setTaskType_id(cursor.getInt(0));
                counter.setTask_id_value(cursor.getInt(1));
                exceededList.add(counter);
            }
            while (cursor.moveToNext());
        }
        db.close();
        return exceededList;
    }

    public void addExceeded(TaskType new_taskType) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
        //setting the current time for the changed date
        ContentValues contentValues = new ContentValues();
        contentValues.put(EXCEEDED_ID_VALUE, new_taskType.getTask_id_value());
        db.insert(EXCEEDED_TASKS_TABLE_NAME,null,contentValues);
        db.close();
    }

    public void updateExceeded(TaskType new_taskType){
        deleteExceeded(new_taskType);
        addExceeded(new_taskType);

        /*
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(EXCEEDED_ID_VALUE, counter.getCounterValue());

        db.update(EXCEEDED_TASKS_TABLE_NAME,contentValues,EXCEEDED_ID + " =?", new String[]{valueOf(counter.getCounterID())});
        db.close();
         */
    }
    public void deleteExceeded(TaskType new_taskType) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        db.delete(EXCEEDED_TASKS_TABLE_NAME, EXCEEDED_ID + " =?", new String[]{valueOf(new_taskType.getTask_id_value())});
        db.close();
    }

    public void deleteAllExceeded(){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        String sql_Query = " DELETE FROM " + EXCEEDED_TASKS_TABLE_NAME;

        db.execSQL(sql_Query);
        db.close();
    }

    public ArrayList<Task> getExceededTasks(){
        ArrayList<TaskType> exceededList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + EXCEEDED_TASKS_TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(sql_Query,null);

        TaskType counter = null;
        if(cursor.moveToFirst()){
            do{
                counter = new TaskType();
                counter.setTaskType_id(cursor.getInt(0));
                counter.setTask_id_value(cursor.getInt(1));
                exceededList.add(counter);
            }
            while (cursor.moveToNext());
        }
        ArrayList<Task> exceededTasks = new ArrayList<>();
        for (TaskType taskType:exceededList) {
            exceededTasks.add(getTaskById(taskType.getTask_id_value()));
        }

        sqLiteDatabase.close();
        return exceededTasks;
    }
/*----------------------------------InProgressTasks-----------------------------------------------*/
public int getInProgressCount() {
    SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

    String sql_Query = "SELECT * FROM " + IN_PROGRESS_TASKS_TABLE_NAME;
    Cursor cursor = db.rawQuery(sql_Query, null);

    int count = cursor.getCount();
    db.close();
    return count;
}

    public TaskType getInProgressByID(int id){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        TaskType taskType = null;
        Cursor cursor = db.query(IN_PROGRESS_TASKS_TABLE_NAME,new String[]{IN_PROGRESS_ID,IN_PROGRESS_ID_VALUE},
                IN_PROGRESS_ID + "=?", new String[]{valueOf(id)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            taskType = new TaskType(cursor.getInt(0),
                    cursor.getInt(1));
        }
        db.close();
        return taskType;
    }

    public TaskType getInProgressByValue(int value){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        TaskType taskType = null;
        Cursor cursor = db.query(IN_PROGRESS_TASKS_TABLE_NAME,new String[]{IN_PROGRESS_ID,IN_PROGRESS_ID_VALUE},
                IN_PROGRESS_ID_VALUE + "=?", new String[]{valueOf(value)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            taskType = new TaskType(cursor.getInt(0),
                    cursor.getInt(1));
        }
        db.close();
        return taskType;
    }


    public ArrayList<TaskType> getAllInProgress() {
        ArrayList<TaskType> inProgressList = new ArrayList<>();
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + IN_PROGRESS_TASKS_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query,null);

        TaskType counter = null;
        if(cursor.moveToFirst()){
            do{
                counter = new TaskType();
                counter.setTaskType_id(cursor.getInt(0));
                counter.setTask_id_value(cursor.getInt(1));
                inProgressList.add(counter);
            }
            while (cursor.moveToNext());
        }
        db.close();
        return inProgressList;
    }

    public void addInProgress(TaskType new_taskType) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
        //setting the current time for the changed date
        ContentValues contentValues = new ContentValues();
        contentValues.put(IN_PROGRESS_ID_VALUE, new_taskType.getTask_id_value());
        db.insert(IN_PROGRESS_TASKS_TABLE_NAME,null,contentValues);
        db.close();
    }

    public void updateInProgress(TaskType new_taskType){
        deleteInProgress(new_taskType);
        addInProgress(new_taskType);

        /*
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(IN_PROGRESS_ID_VALUE, counter.getCounterValue());

        db.update(IN_PROGRESS_TASKS_TABLE_NAME,contentValues,IN_PROGRESS_ID + " =?", new String[]{valueOf(counter.getCounterID())});
        db.close();
         */
    }
    public void deleteInProgress(TaskType new_taskType) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        db.delete(IN_PROGRESS_TASKS_TABLE_NAME, IN_PROGRESS_ID + " =?", new String[]{valueOf(new_taskType.getTask_id_value())});
        db.close();
    }

    public void deleteAllInProgress(){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        String sql_Query = " DELETE FROM " + IN_PROGRESS_TASKS_TABLE_NAME;

        db.execSQL(sql_Query);
        db.close();
    }

    public ArrayList<Task> getInProgressTasks(){
        //ArrayList<TaskType> inProgressList = new ArrayList<>();
        ArrayList<Task> inProgressTasks = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + IN_PROGRESS_TASKS_TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(sql_Query,null);

        TaskType counter = null;
        if(cursor.moveToFirst()){
            do{
                counter = new TaskType();
                counter.setTaskType_id(cursor.getInt(0));
                counter.setTask_id_value(cursor.getInt(1));
                //inProgressList.add(counter);
                inProgressTasks.add(getTaskById(counter.getTask_id_value()));

            }
            while (cursor.moveToNext());
        }
        /*
        ArrayList<Task> inProgressTasks = new ArrayList<>();
        for (TaskType taskType:inProgressList) {
            inProgressTasks.add(getTaskById(taskType.getTask_id_value()));
        }
         */

        sqLiteDatabase.close();
        return inProgressTasks;
    }
/*-----------------------------------CompletedTasks------------------------------------------------*/
public int getCompletedCount() {
    SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

    String sql_Query = "SELECT * FROM " + COMPLETED_TASKS_TABLE;
    Cursor cursor = db.rawQuery(sql_Query, null);

    int count = cursor.getCount();
    db.close();
    return count;
}

    public TaskType getCompletedByID(int id){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        TaskType taskType = null;
        Cursor cursor = db.query(COMPLETED_TASKS_TABLE,new String[]{COMPLETED_ID,COMPLETED_ID_VALUE},
                COMPLETED_ID + "=?", new String[]{valueOf(id)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            taskType = new TaskType(cursor.getInt(0),
                    cursor.getInt(1));
        }
        db.close();
        return taskType;
    }

    public TaskType getCompletedByValue(int value){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        TaskType taskType = null;
        Cursor cursor = db.query(COMPLETED_TASKS_TABLE,new String[]{COMPLETED_ID,COMPLETED_ID_VALUE},
                COMPLETED_ID_VALUE + "=?", new String[]{valueOf(value)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            taskType = new TaskType(cursor.getInt(0),
                    cursor.getInt(1));
        }
        db.close();
        return taskType;
    }


    public ArrayList<TaskType> getAllCompleted() {
        ArrayList<TaskType> completedList = new ArrayList<>();
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + COMPLETED_TASKS_TABLE;
        Cursor cursor = db.rawQuery(sql_Query,null);

        TaskType counter = null;
        if(cursor.moveToFirst()){
            do{
                counter = new TaskType();
                counter.setTaskType_id(cursor.getInt(0));
                counter.setTask_id_value(cursor.getInt(1));
                completedList.add(counter);
            }
            while (cursor.moveToNext());
        }
        db.close();
        return completedList;
    }

    public void addCompleted(TaskType new_taskType) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
        //setting the current time for the changed date
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMPLETED_ID_VALUE, new_taskType.getTask_id_value());
        db.insert(COMPLETED_TASKS_TABLE,null,contentValues);
        db.close();
    }

    public void updateCompleted(TaskType new_taskType){
        deleteCompleted(new_taskType);
        addCompleted(new_taskType);

        /*
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COMPLETED_ID_VALUE, counter.getCounterValue());

        db.update(COMPLETED_TASKS_TABLE,contentValues,COMPLETED_ID + " =?", new String[]{valueOf(counter.getCounterID())});
        db.close();
         */
    }
    public void deleteCompleted(TaskType new_taskType) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        db.delete(COMPLETED_TASKS_TABLE, COMPLETED_ID + " =?", new String[]{valueOf(new_taskType.getTask_id_value())});
        db.close();
    }

    public void deleteAllCompleted(){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        String sql_Query = " DELETE FROM " + COMPLETED_TASKS_TABLE;

        db.execSQL(sql_Query);
        db.close();
    }

    public ArrayList<Task> getCompletedTasks(){
        ArrayList<TaskType> completedList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + COMPLETED_TASKS_TABLE;
        Cursor cursor = sqLiteDatabase.rawQuery(sql_Query,null);

        TaskType counter = null;
        if(cursor.moveToFirst()){
            do{
                counter = new TaskType();
                counter.setTaskType_id(cursor.getInt(0));
                counter.setTask_id_value(cursor.getInt(1));
                completedList.add(counter);
            }
            while (cursor.moveToNext());
        }
        ArrayList<Task> completedTasks = new ArrayList<>();
        for (TaskType taskType:completedList) {
            completedTasks.add(getTaskById(taskType.getTask_id_value()));
        }

        sqLiteDatabase.close();
        return completedTasks;
    }


}