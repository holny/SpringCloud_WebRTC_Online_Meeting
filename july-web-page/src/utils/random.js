import {isNotEmpty, isNumerical} from "@/utils/validate";

let COLOR=['secondary','accent','pink','teal','orange','red','purple','deep-purple','indigo','blue','light-blue','cyan','green','light-green','lime','yellow','amber','orange','deep-orange','brown','blue-grey']
let FADE_BUSY_COLOR=['light-green-13','light-green-13','green-13','light-green-6','green-6','lime-7','yellow-7','amber','orange','deep-orange','red']
export function randomColor (index,exclude) {
    // Number.isFinite(index)
    if(isNotEmpty(index)&& isNumerical(index)){
        index =  parseInt(index);
        let c = COLOR[index%COLOR.length]
        if(isNotEmpty(exclude)){
            let count = 0
            while (exclude.indexOf(c)>=0&&count<index&COLOR.length){
                count+=1
                index+=1
                c = COLOR[index&COLOR.length]
            }
        }
        return c
    }else{
        let c = COLOR[Math.floor((Math.random()*COLOR.length))]
        return c
    }
}

export function getQuasarColorByRatio(ratio) {
    if(ratio>1){
        ratio = ratio/100
    }
    let c = FADE_BUSY_COLOR[Math.floor((ratio*(FADE_BUSY_COLOR.length-1)))]
    return c

}

export function getRGBColorByRatio(bili){
    //var 百分之一 = (单色值范围) / 50;  单颜色的变化范围只在50%之内
    var one = (255+255) / 100;
    var r=0;
    var g=0;
    var b=0;

    if ( bili < 50 ) {
        // 比例小于50的时候红色是越来越多的,直到红色为255时(红+绿)变为黄色.
        r = one * bili;
        g=255;
    }
    if ( bili >= 50 ) {
        // 比例大于50的时候绿色是越来越少的,直到0 变为纯红
        g =  255 - ( (bili - 50 ) * one) ;
        r = 255;
    }
    r = parseInt(r);// 取整
    g = parseInt(g);// 取整
    b = parseInt(b);// 取整

    //console.log("#"+r.toString(16,2)+g.toString(16,2)+b.toString(16,2));
    //return "#"+r.toString(16,2)+g.toString(16,2)+b.toString(16,2);
    //console.log("rgb("+r+","+g+","+b+")" );
    return "rgb("+r+","+g+","+b+")";

}
